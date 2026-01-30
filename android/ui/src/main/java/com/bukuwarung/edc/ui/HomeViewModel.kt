package com.bukuwarung.edc.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _uiEvents = MutableSharedFlow<HomeUiEvent>(extraBufferCapacity = 1)
    val uiEvents: SharedFlow<HomeUiEvent> = _uiEvents.asSharedFlow()

    fun onActionClick(action: HomeAction) {
        when (action) {
            HomeAction.Transfer -> _uiEvents.tryEmit(HomeUiEvent.NavigateToTransfer)
            HomeAction.CekSaldo -> _uiEvents.tryEmit(HomeUiEvent.NavigateToBalanceCheck)
            HomeAction.Pengaturan -> _uiEvents.tryEmit(HomeUiEvent.NavigateToSettings)
            else -> _uiEvents.tryEmit(HomeUiEvent.ShowToast(action))
        }
    }
}

sealed interface HomeUiEvent {
    data class ShowToast(val action: HomeAction) : HomeUiEvent
    data object NavigateToTransfer : HomeUiEvent
    data object NavigateToBalanceCheck : HomeUiEvent
    data object NavigateToSettings : HomeUiEvent
}