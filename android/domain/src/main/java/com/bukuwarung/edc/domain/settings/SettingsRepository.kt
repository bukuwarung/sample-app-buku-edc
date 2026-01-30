package com.bukuwarung.edc.domain.settings

interface SettingsRepository {
    suspend fun getAccountSettings(): AccountSettings
    suspend fun getBankAccounts(): List<BankAccount>
}
