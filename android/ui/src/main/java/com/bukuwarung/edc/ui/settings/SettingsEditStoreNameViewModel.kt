package com.bukuwarung.edc.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bukuwarung.edc.domain.settings.GetAccountSettingsUseCase
import com.bukuwarung.edc.ui.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsEditStoreNameViewModel @Inject constructor(
    private val getAccountSettings: GetAccountSettingsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsEditStoreNameUiState())
    val state: StateFlow<SettingsEditStoreNameUiState> = _state.asStateFlow()

    private val _uiEvents = MutableSharedFlow<SettingsEditStoreNameUiEvent>(extraBufferCapacity = 1)
    val uiEvents: SharedFlow<SettingsEditStoreNameUiEvent> = _uiEvents.asSharedFlow()

    init {
        viewModelScope.launch {
            val settings = getAccountSettings()
            _state.value = SettingsEditStoreNameUiState(
                initialStoreName = settings.storeInfo.name,
                storeNameInput = settings.storeInfo.name,
                storeAddress = settings.storeInfo.address,
            )
        }
    }

    fun onStoreNameChange(value: String) {
        _state.value = _state.value.copy(storeNameInput = value)
    }

    fun onSaveClick() {
        // Non-functional: no persistence/state mutation beyond current screen.
        _uiEvents.tryEmit(SettingsEditStoreNameUiEvent.ShowToastAndClose(R.string.settings_toast_store_name_updated))
    }
}

data class SettingsEditStoreNameUiState(
    val initialStoreName: String = "",
    val storeNameInput: String = "",
    val storeAddress: String = "",
) {
    val isSaveEnabled: Boolean =
        storeNameInput.isNotBlank() && storeNameInput != initialStoreName
}

sealed interface SettingsEditStoreNameUiEvent {
    data class ShowToastAndClose(val messageResId: Int) : SettingsEditStoreNameUiEvent
}
