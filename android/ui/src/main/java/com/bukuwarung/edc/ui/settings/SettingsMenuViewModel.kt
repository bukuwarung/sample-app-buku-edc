package com.bukuwarung.edc.ui.settings

import androidx.lifecycle.ViewModel
import com.bukuwarung.edc.ui.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class SettingsMenuViewModel @Inject constructor() : ViewModel() {
    private val _uiEvents = MutableSharedFlow<SettingsMenuUiEvent>(extraBufferCapacity = 1)
    val uiEvents: SharedFlow<SettingsMenuUiEvent> = _uiEvents.asSharedFlow()

    fun onPrintReceiptTestClick() {
        _uiEvents.tryEmit(SettingsMenuUiEvent.ShowToast(R.string.settings_toast_feature_unavailable))
    }

    fun onSoundEffectSettingsClick() {
        _uiEvents.tryEmit(SettingsMenuUiEvent.ShowToast(R.string.settings_toast_feature_unavailable))
    }
}

sealed interface SettingsMenuUiEvent {
    data class ShowToast(val messageResId: Int) : SettingsMenuUiEvent
}
