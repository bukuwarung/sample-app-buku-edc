package com.bukuwarung.edc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bukuwarung.edc.domain.cash.CheckCashWithdrawalEligibilityUseCase
import com.bukuwarung.edc.domain.cash.CheckIsFirstTimeUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val checkCashWithdrawalEligibilityUseCase: CheckCashWithdrawalEligibilityUseCase,
    private val checkIsFirstTimeUserUseCase: CheckIsFirstTimeUserUseCase
) : ViewModel() {
    private val _uiEvents = MutableSharedFlow<HomeUiEvent>(extraBufferCapacity = 1)
    val uiEvents: SharedFlow<HomeUiEvent> = _uiEvents.asSharedFlow()

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

                HomeAction.Pengaturan -> _uiEvents.emit(HomeUiEvent.NavigateToSettings)
                HomeAction.AtmTest -> _uiEvents.emit(HomeUiEvent.NavigateToAtmTest)
                else -> _uiEvents.emit(HomeUiEvent.ShowToast(action))
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
    data object NavigateToSettings : HomeUiEvent
    data object NavigateToAtmTest : HomeUiEvent
}
