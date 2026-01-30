package com.bukuwarung.edc.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bukuwarung.edc.domain.settings.AccountSettings
import com.bukuwarung.edc.domain.settings.GetAccountSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsAccountViewModel @Inject constructor(
    private val getAccountSettings: GetAccountSettingsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsAccountUiState())
    val state: StateFlow<SettingsAccountUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val settings = getAccountSettings()
            _state.value = SettingsAccountUiState(accountSettings = settings)
        }
    }
}

data class SettingsAccountUiState(
    val accountSettings: AccountSettings? = null,
)
