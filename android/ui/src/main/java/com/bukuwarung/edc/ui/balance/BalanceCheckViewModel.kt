package com.bukuwarung.edc.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bukuwarung.edc.domain.settings.SettingsRepository
import com.bukuwarung.edc.domain.transaction.BalanceInfo
import com.bukuwarung.edc.domain.transaction.BalanceRepository
import com.bukuwarung.edc.sdk.exceptions.BackendException
import com.bukuwarung.edc.sdk.exceptions.DeviceSdkException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel for the Balance Check summary and receipt screens.
 *
 * Partners: This ViewModel demonstrates the balance check SDK integration:
 *
 * 1. On initialization, it calls [BalanceRepository.checkBalance] with the account type
 *    selected by the user (stored in [BalanceCheckFlowStateHolder]).
 *
 * 2. On success, the [BalanceInfo] result (mapped from `CardReceiptResponse`) is stored
 *    in the flow state holder so both the summary and receipt screens can read from it.
 *
 * 3. The SDK's `checkBalance()` handles card reading, PIN entry, and backend communication
 *    automatically — the partner only provides `accountId` and `accountType`.
 *
 * Error handling covers SDK exception types:
 * - [DeviceSdkException] — EDC device errors (card read, PIN cancelled, timeout)
 * - [BackendException] — backend processing errors (format error, invalid PIN, invalid merchant)
 */
@HiltViewModel
class BalanceCheckViewModel @Inject constructor(
    private val balanceRepository: BalanceRepository,
    private val flowState: BalanceCheckFlowStateHolder,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    // ——————————————————————————————————————————————————————————————
    // UI state
    // ——————————————————————————————————————————————————————————————

    sealed class BalanceUiState {
        /** Balance check is in progress — show loading indicator. */
        data object Loading : BalanceUiState()

        /**
         * Balance check succeeded — display balance information.
         *
         * Partners: All fields are populated from the SDK's CardReceiptResponse
         * returned by `AtmFeatures.checkBalance()`.
         */
        data class Success(
            val balanceAmount: String,
            val timestamp: String,
            val refNo: String,
            val accountType: String,
            val cardNumber: String,
            val cardHolderName: String,
            val bankName: String
        ) : BalanceUiState()

        /**
         * Balance check failed — display error message with optional retry.
         */
        data class Error(val message: String) : BalanceUiState()
    }

    private val _uiState = MutableStateFlow<BalanceUiState>(BalanceUiState.Loading)
    val uiState: StateFlow<BalanceUiState> = _uiState.asStateFlow()

    init {
        performBalanceCheck()
    }

    // ——————————————————————————————————————————————————————————————
    // Balance Check
    // Partners: The balance check is called automatically when the Summary screen opens.
    // The SDK handles card reading and PIN entry internally — the call only needs
    // accountId and accountType.
    //
    // Note (SDK 0.1.3): The `sourceDetails: BankDetails` parameter was removed.
    // The current signature is `checkBalance(accountId: String, accountType: AccountType)`.
    // ——————————————————————————————————————————————————————————————

    private fun performBalanceCheck() {
        viewModelScope.launch {
            _uiState.value = BalanceUiState.Loading

            // Partners: accountId is the merchant's account UUID, configured in Developer Settings.
            // In production, this would come from your auth/account service.
            val accountId = settingsRepository.getAccountId().first().trim()
                .ifEmpty { "no-account-id-configured" }

            balanceRepository.checkBalance(
                accountId = accountId,
                accountType = flowState.accountType
            ).onSuccess { balanceInfo ->
                // Partners: Save the result in the flow state holder so the receipt
                // screen can also access it (each screen creates its own ViewModel).
                flowState.balanceResult = balanceInfo

                _uiState.value = BalanceUiState.Success(
                    balanceAmount = formatRupiah(balanceInfo.totalAmount),
                    timestamp = formatTimestamp(balanceInfo),
                    refNo = balanceInfo.rrn,
                    accountType = balanceInfo.accountType,
                    cardNumber = balanceInfo.cardNumber,
                    cardHolderName = balanceInfo.cardHolderName,
                    bankName = balanceInfo.bankName
                )
            }.onFailure { error ->
                _uiState.value = BalanceUiState.Error(
                    message = getErrorMessage(error)
                )
            }
        }
    }

    /** Retries the balance check (e.g. after a transient error). */
    fun retry() {
        performBalanceCheck()
    }

    /** Clears the flow state when the user leaves the balance check flow. */
    fun clearFlowState() {
        flowState.clear()
    }

    /**
     * Maps SDK exceptions to user-facing error messages.
     *
     * Partners: The SDK throws specific exception types for different failure modes.
     * Handle each type to provide actionable guidance to the user:
     * - [DeviceSdkException] → device/hardware issue (card read error, PIN cancelled, etc.)
     * - [BackendException] → backend processing error (invalid PIN, format error, etc.)
     */
    private fun getErrorMessage(error: Throwable): String = when (error) {
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

    private fun formatTimestamp(balanceInfo: BalanceInfo): String {
        return SimpleDateFormat("dd MMM yyyy HH:mm", Locale("id", "ID"))
            .format(balanceInfo.timestamp)
    }
}
