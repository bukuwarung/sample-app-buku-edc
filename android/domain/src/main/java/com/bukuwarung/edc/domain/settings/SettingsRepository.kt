package com.bukuwarung.edc.domain.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getAccountSettings(): AccountSettings
    suspend fun getBankAccounts(): List<BankAccount>
    fun isFirstTimeUser(): Flow<Boolean>
    suspend fun setIsFirstTimeUser(isFirstTime: Boolean)
}
