package com.bukuwarung.edc.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class HomeViewModel : ViewModel() {
    private val _uiEvents = MutableSharedFlow<HomeUiEvent>(extraBufferCapacity = 1)
    val uiEvents: SharedFlow<HomeUiEvent> = _uiEvents.asSharedFlow()

    fun onActionClick(action: HomeAction) {
        if (action == HomeAction.Transfer) {
            _uiEvents.tryEmit(HomeUiEvent.NavigateToTransfer)
        } else {
            _uiEvents.tryEmit(HomeUiEvent.ShowToast(action))
        }
    }
}

sealed interface HomeUiEvent {
    data class ShowToast(val action: HomeAction) : HomeUiEvent
    data object NavigateToTransfer : HomeUiEvent
}