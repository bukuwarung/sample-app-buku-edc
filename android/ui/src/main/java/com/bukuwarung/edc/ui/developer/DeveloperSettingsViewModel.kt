package com.bukuwarung.edc.ui.developer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bukuwarung.edc.domain.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeveloperSettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val isFirstTimeUser: StateFlow<Boolean> = settingsRepository.isFirstTimeUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun setFirstTimeUser(isFirstTime: Boolean) {
        viewModelScope.launch {
            settingsRepository.setIsFirstTimeUser(isFirstTime)
        }
    }
}
