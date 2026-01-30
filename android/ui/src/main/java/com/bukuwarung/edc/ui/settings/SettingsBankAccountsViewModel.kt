package com.bukuwarung.edc.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bukuwarung.edc.domain.settings.BankAccount
import com.bukuwarung.edc.domain.settings.GetBankAccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsBankAccountsViewModel @Inject constructor(
    private val getBankAccounts: GetBankAccountsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsBankAccountsUiState())
    val state: StateFlow<SettingsBankAccountsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(accounts = getBankAccounts())
        }
    }
}

data class SettingsBankAccountsUiState(
    val accounts: List<BankAccount> = emptyList(),
)
