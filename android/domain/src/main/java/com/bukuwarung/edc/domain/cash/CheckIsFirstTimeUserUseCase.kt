package com.bukuwarung.edc.domain.cash

import com.bukuwarung.edc.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckIsFirstTimeUserUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Boolean {
        return settingsRepository.isFirstTimeUser().first()
    }
}
