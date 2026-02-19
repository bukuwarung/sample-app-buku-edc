package com.bukuwarung.edc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bukuwarung.edc.domain.cash.CheckCashWithdrawalEligibilityUseCase
import com.bukuwarung.edc.domain.cash.CheckIsFirstTimeUserUseCase
import com.bukuwarung.edc.domain.transaction.CardRepository
import com.bukuwarung.edc.domain.transaction.IncompleteTransactionInfo
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
 * Partners: HomeViewModel checks for incomplete transactions on app start via
 * [CardRepository.checkIncompleteTransactions]. If a pending transaction is found,
 * the dialog state is exposed via [incompleteTransaction] for the UI to display
 * a prompt allowing the user to resume or dismiss.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val checkCashWithdrawalEligibilityUseCase: CheckCashWithdrawalEligibilityUseCase,
    private val checkIsFirstTimeUserUseCase: CheckIsFirstTimeUserUseCase,
    private val cardRepository: CardRepository
) : ViewModel() {
    private val _uiEvents = MutableSharedFlow<HomeUiEvent>(extraBufferCapacity = 1)
    val uiEvents: SharedFlow<HomeUiEvent> = _uiEvents.asSharedFlow()

    // ——————————————————————————————————————————————————————————————
    // Incomplete Transaction Detection
    // Partners: Call checkIncompleteTransactions() on app start to detect any
    // pending transactions from a previous session. If found, show a dialog
    // prompting the user to acknowledge the pending transaction.
    // ——————————————————————————————————————————————————————————————

    private val _incompleteTransaction = MutableStateFlow<IncompleteTransactionInfo?>(null)
    val incompleteTransaction: StateFlow<IncompleteTransactionInfo?> = _incompleteTransaction.asStateFlow()

    init {
        checkIncompleteTransactions()
    }

    /**
     * Partners: Called automatically on ViewModel creation. If a non-null
     * [IncompleteTransactionInfo] is returned, the UI should show a dialog
     * with the transaction details (type, amount, transactionId).
     *
     * Errors are silently ignored — incomplete transaction check is best-effort
     * and should not block the user from using the app.
     */
    private fun checkIncompleteTransactions() {
        viewModelScope.launch {
            cardRepository.checkIncompleteTransactions()
                .onSuccess { incomplete ->
                    _incompleteTransaction.value = incomplete
                }
        }
    }

    /** Partners: Call this when the user dismisses the incomplete transaction dialog. */
    fun dismissIncompleteTransaction() {
        _incompleteTransaction.value = null
    }

    fun onActionClick(action: HomeAction) {
        viewModelScope.launch {
            when (action) {
                HomeAction.Transfer -> {
                    if (checkIsFirstTimeUserUseCase()) {
                        _uiEvents.emit(HomeUiEvent.NavigateToFirstTimeUserPrompt)
                    } else {
                        _uiEvents.emit(HomeUiEvent.NavigateToTransfer)
                    }
                }

                HomeAction.CekSaldo -> {
                    if (checkIsFirstTimeUserUseCase()) {
                        _uiEvents.emit(HomeUiEvent.NavigateToFirstTimeUserPrompt)
                    } else {
                        _uiEvents.emit(HomeUiEvent.NavigateToBalanceCheck)
                    }
                }

                HomeAction.TarikTunai -> {
                    if (checkIsFirstTimeUserUseCase()) {
                        _uiEvents.emit(HomeUiEvent.NavigateToFirstTimeUserPrompt)
                    } else {
                        if (checkCashWithdrawalEligibilityUseCase()) {
                            _uiEvents.emit(HomeUiEvent.NavigateToCashWithdrawal)
                        } else {
                            _uiEvents.emit(HomeUiEvent.NavigateToAddBankAccount)
                        }
                    }
                }

                HomeAction.Riwayat -> _uiEvents.emit(HomeUiEvent.NavigateToHistory)
                HomeAction.Pengaturan -> _uiEvents.emit(HomeUiEvent.NavigateToSettings)
            }
        }
    }
}

sealed interface HomeUiEvent {
    data class ShowToast(val action: HomeAction) : HomeUiEvent
    data object NavigateToTransfer : HomeUiEvent
    data object NavigateToBalanceCheck : HomeUiEvent
    data object NavigateToCashWithdrawal : HomeUiEvent
    data object NavigateToFirstTimeUserPrompt : HomeUiEvent
    data object NavigateToAddBankAccount : HomeUiEvent
    data object NavigateToHistory : HomeUiEvent
    data object NavigateToSettings : HomeUiEvent
}