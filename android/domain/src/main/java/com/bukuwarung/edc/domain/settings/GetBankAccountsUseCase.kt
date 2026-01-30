package com.bukuwarung.edc.domain.settings

import javax.inject.Inject

class GetBankAccountsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(): List<BankAccount> = repository.getBankAccounts()
}
