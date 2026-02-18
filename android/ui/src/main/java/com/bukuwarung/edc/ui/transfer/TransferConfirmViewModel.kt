package com.bukuwarung.edc.ui.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bukuwarung.edc.domain.transaction.TransactionEvent
import com.bukuwarung.edc.domain.transaction.TransactionEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Confirm screen.
 *
 * Shows a summary of the transaction details and observes
 * [TransactionEventRepository.transactionEvents] for SDK processing lifecycle events
 * that happen AFTER the user taps "Transfer":
 *
 * - [TransactionEvent.ProcessingTransaction] → show processing overlay
 * - [TransactionEvent.TransactionComplete]   → emit [UiEvent.NavigateToSuccess]
 * - [TransactionEvent.TransactionFailed]     → emit [UiEvent.ShowError]
 *
 * **Partners:** The actual transaction call (`transferPosting`) will be triggered here
 * in the Transfer flow tasks. For now the events are observed so the UI is ready.
 */
@HiltViewModel
class TransferConfirmViewModel @Inject constructor(
    private val transactionEventRepository: TransactionEventRepository
) : ViewModel() {

    // ——————————————————————————————————————————————————————————————
    // Mocked transaction details — will be replaced with real data
    // from TransferRepository.transferInquiry() in Task 4.
    // ——————————————————————————————————————————————————————————————
    val type = "Transfer"
    val bankName = "BCA"
    val accountNo = "8372131455"
    val accountName = "Pramudiana"
    val amount = "Rp1.000.000"
    val remark = "Pembayaran Hutang"

    // ——————————————————————————————————————————————————————————————
    // One-shot UI events
    // ——————————————————————————————————————————————————————————————

    sealed class UiEvent {
        /** SDK reported TransactionComplete — navigate to success screen. */
        data object NavigateToSuccess : UiEvent()

        /**
         * SDK reported TransactionFailed — show error dialog/snackbar.
         *
         * @property message Human-readable error description.
         * @property canRetry Whether to offer a retry option.
         */
        data class ShowError(val message: String, val canRetry: Boolean) : UiEvent()
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
                        _uiEvent.emit(UiEvent.NavigateToSuccess)
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
}
