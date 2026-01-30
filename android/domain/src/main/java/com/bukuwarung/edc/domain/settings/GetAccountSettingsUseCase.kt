package com.bukuwarung.edc.domain.settings

import javax.inject.Inject

class GetAccountSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(): AccountSettings = repository.getAccountSettings()
}
