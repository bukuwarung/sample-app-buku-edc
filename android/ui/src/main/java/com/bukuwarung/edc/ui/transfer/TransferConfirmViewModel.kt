package com.bukuwarung.edc.ui.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bukuwarung.edc.domain.transaction.TransactionEvent
import com.bukuwarung.edc.domain.transaction.TransactionEventRepository
import com.bukuwarung.edc.domain.transaction.TransferRepository
import com.bukuwarung.edc.sdk.exceptions.BackendException
import com.bukuwarung.edc.sdk.exceptions.DeviceSdkException
import com.bukuwarung.edc.sdk.exceptions.InvalidTokenException
import com.bukuwarung.edc.sdk.exceptions.TokenExpiredException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel for the Confirm screen — orchestrates the two-step transfer flow.
 *
 * Partners: This ViewModel demonstrates the core SDK transfer pattern:
 *
 * 1. **On initialization** — calls [TransferRepository.transferInquiry] with the transfer
 *    details accumulated in [TransferFlowStateHolder]. The inquiry returns fee information
 *    (amount, adminFee, totalAmount) and a single-use `transactionToken`.
 *
 * 2. **On user confirmation** — calls [TransferRepository.transferPosting] with the token
 *    from step 1. The token is valid for 15 minutes and can only be used once.
 *
 * The ViewModel also observes [TransactionEventRepository.transactionEvents] for SDK
 * processing lifecycle events (ProcessingTransaction, TransactionComplete, TransactionFailed)
 * to drive the loading overlay UI.
 *
 * Error handling covers all SDK exception types:
 * - [TokenExpiredException] — token exceeded 15-minute window, user must redo inquiry
 * - [InvalidTokenException] — token already used or malformed
 * - [DeviceSdkException] — EDC device errors (card read, PIN cancelled, timeout)
 * - [BackendException] — backend processing errors (format error, invalid PIN, invalid merchant)
 */
@HiltViewModel
class TransferConfirmViewModel @Inject constructor(
    private val transactionEventRepository: TransactionEventRepository,
    private val transferRepository: TransferRepository,
    private val flowState: TransferFlowStateHolder
) : ViewModel() {

    // ——————————————————————————————————————————————————————————————
    // UI state for the confirmation screen
    // ——————————————————————————————————————————————————————————————

    sealed class ConfirmUiState {
        /** Inquiry is in progress — show loading indicator. */
        data object Loading : ConfirmUiState()

        /**
         * Inquiry succeeded — display fee breakdown for user confirmation.
         *
         * Partners: All fields are populated from the SDK's CardReceiptResponse
         * returned by `AtmFeatures.transferInquiry()`.
         */
        data class InquirySuccess(
            val type: String,
            val bankName: String,
            val accountNo: String,
            val accountName: String,
            val amount: String,
            val adminFee: String,
            val totalAmount: String,
            val remark: String
        ) : ConfirmUiState()

        /**
         * Inquiry or posting failed.
         *
         * @property isTokenError If true, the user needs to navigate back and
         *           redo the transfer from the beginning (token expired/invalid).
         */
        data class Error(val message: String, val isTokenError: Boolean = false) : ConfirmUiState()
    }

    private val _uiState = MutableStateFlow<ConfirmUiState>(ConfirmUiState.Loading)
    val uiState: StateFlow<ConfirmUiState> = _uiState.asStateFlow()

    // ——————————————————————————————————————————————————————————————
    // One-shot UI events
    // ——————————————————————————————————————————————————————————————

    sealed class UiEvent {
        /** Transfer posting succeeded — navigate to success screen. */
        data object NavigateToSuccess : UiEvent()

        /**
         * An error occurred during posting.
         *
         * @property message Human-readable error description.
         * @property canRetry Whether to offer a retry option.
         * @property isTokenError If true, the user should redo the inquiry step.
         */
        data class ShowError(
            val message: String,
            val canRetry: Boolean,
            val isTokenError: Boolean = false
        ) : UiEvent()
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    // ——————————————————————————————————————————————————————————————
    // Processing state — drives the loading overlay on the screen
    // ——————————————————————————————————————————————————————————————

    data class ProcessingState(
        val isProcessing: Boolean = false,
        val currentStep: String = ""
    )

    private val _processingState = MutableStateFlow(ProcessingState())
    val processingState: StateFlow<ProcessingState> = _processingState.asStateFlow()

    init {
        observeTransactionEvents()
        performInquiry()
    }

    // ——————————————————————————————————————————————————————————————
    // Step 1: Transfer Inquiry
    // Partners: The inquiry is called automatically when the Confirm screen opens.
    // On success, the response populates the confirmation UI with fee breakdown.
    // The transactionToken is saved in flowState.inquiryReceipt for the posting step.
    // ——————————————————————————————————————————————————————————————

    private fun performInquiry() {
        viewModelScope.launch {
            _uiState.value = ConfirmUiState.Loading

            // Partners: Parse the user-entered amount string to BigInteger.
            // In production, use a proper currency input parser.
            val amountBigInt = flowState.amount.filter { it.isDigit() }
                .toBigIntegerOrNull() ?: BigInteger.ZERO

            transferRepository.transferInquiry(
                accountId = flowState.accountNumber,
                amount = amountBigInt,
                bankCode = flowState.bankCode,
                bankName = flowState.bankName,
                notes = flowState.notes,
                isCashWithdrawal = false,
                accountType = flowState.accountType
            ).onSuccess { receipt ->
                // Partners: Save the inquiry receipt — its transactionToken is needed
                // for transferPosting() in the next step.
                flowState.inquiryReceipt = receipt

                _uiState.value = ConfirmUiState.InquirySuccess(
                    type = "Transfer",
                    bankName = receipt.bankName.ifEmpty { flowState.bankName },
                    accountNo = flowState.accountNumber,
                    accountName = receipt.cardHolderName,
                    amount = formatRupiah(receipt.amount),
                    adminFee = formatRupiah(receipt.adminFee),
                    totalAmount = formatRupiah(receipt.totalAmount),
                    remark = flowState.notes
                )
            }.onFailure { error ->
                _uiState.value = ConfirmUiState.Error(
                    message = getErrorMessage(error),
                    isTokenError = error is TokenExpiredException || error is InvalidTokenException
                )
            }
        }
    }

    // ——————————————————————————————————————————————————————————————
    // Step 2: Transfer Posting
    // Partners: This is triggered when the user taps the "Transfer" button on the
    // confirmation screen. The single-use transactionToken from the inquiry step is
    // sent to the SDK to execute the actual fund transfer.
    //
    // If the token has expired (>15 min since inquiry), the SDK throws
    // TokenExpiredException and the user must navigate back to redo the inquiry.
    // ——————————————————————————————————————————————————————————————

    fun confirmTransfer() {
        val receipt = flowState.inquiryReceipt ?: return
        val token = receipt.transactionToken ?: return

        viewModelScope.launch {
            _processingState.value = ProcessingState(isProcessing = true, currentStep = "")

            transferRepository.transferPosting(
                accountId = flowState.accountNumber,
                transactionToken = token
            ).onSuccess { postingReceipt ->
                // Partners: Save the posting receipt for the success screen to display
                // final transaction details (RRN, approval code, total amount).
                flowState.postingReceipt = postingReceipt
                _processingState.value = ProcessingState(isProcessing = false)
                _uiEvent.emit(UiEvent.NavigateToSuccess)
            }.onFailure { error ->
                _processingState.value = ProcessingState(isProcessing = false)
                _uiEvent.emit(
                    UiEvent.ShowError(
                        message = getErrorMessage(error),
                        canRetry = error !is TokenExpiredException && error !is InvalidTokenException,
                        isTokenError = error is TokenExpiredException || error is InvalidTokenException
                    )
                )
            }
        }
    }

    /** Retries the inquiry step (e.g. after a transient error). */
    fun retryInquiry() {
        performInquiry()
    }

    private fun observeTransactionEvents() {
        viewModelScope.launch {
            transactionEventRepository.transactionEvents.collect { event ->
                when (event) {
                    is TransactionEvent.ProcessingTransaction -> {
                        _processingState.value = ProcessingState(
                            isProcessing = true,
                            currentStep = event.step
                        )
                    }

                    is TransactionEvent.TransactionComplete -> {
                        _processingState.value = ProcessingState(isProcessing = false)
                    }

                    is TransactionEvent.TransactionFailed -> {
                        _processingState.value = ProcessingState(isProcessing = false)
                        _uiEvent.emit(
                            UiEvent.ShowError(
                                message = event.message,
                                canRetry = event.canRetry
                            )
                        )
                    }

                    else -> {} // Other events handled by their own screens
                }
            }
        }
    }

    /**
     * Maps SDK exceptions to user-facing error messages.
     *
     * Partners: The SDK throws specific exception types for different failure modes.
     * Handle each type to provide actionable guidance to the user:
     * - [TokenExpiredException] → prompt user to restart the transfer
     * - [InvalidTokenException] → prompt user to restart the transfer
     * - [DeviceSdkException] → device/hardware issue (card read error, PIN cancelled, etc.)
     * - [BackendException] → backend processing error (invalid PIN, format error, etc.)
     */
    private fun getErrorMessage(error: Throwable): String = when (error) {
        is TokenExpiredException ->
            "Token transaksi sudah kedaluwarsa. Silakan ulangi proses transfer."
        is InvalidTokenException ->
            "Token transaksi tidak valid. Silakan ulangi proses transfer."
        is DeviceSdkException ->
            "Kesalahan perangkat (${error.code}): ${error.message}"
        is BackendException ->
            "Kesalahan server (${error.code}): ${error.message}"
        else ->
            error.message ?: "Terjadi kesalahan. Silakan coba lagi."
    }

    private fun formatRupiah(amount: BigInteger): String {
        val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
        return "Rp${formatter.format(amount)}"
    }
}
