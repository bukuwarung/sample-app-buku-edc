package com.bukuwarung.edc.domain.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getAccountSettings(): AccountSettings
    suspend fun getBankAccounts(): List<BankAccount>
    fun isFirstTimeUser(): Flow<Boolean>
    suspend fun setIsFirstTimeUser(isFirstTime: Boolean)

    /** Returns the stored phone number, or empty string if not set. */
    fun getPhoneNumber(): Flow<String>

    /** Persists the phone number for SDK authentication. */
    suspend fun setPhoneNumber(phoneNumber: String)

    /**
     * Returns the stored access token, or empty string if not set.
     *
     * Partners: This token is obtained from the token exchange API:
     * POST https://api-dev.bukuwarung.com/sdk/v1/token/exchange
     * with partnerId, partnerSecret, and partnerUserToken.
     * The token is valid for 1 hour.
     */
    fun getAccessToken(): Flow<String>

    /** Persists the access token for SDK authentication. */
    suspend fun setAccessToken(accessToken: String)
}
