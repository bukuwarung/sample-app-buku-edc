package com.bukuwarung.edc.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bukuwarung.edc.domain.settings.AccountSettings
import com.bukuwarung.edc.domain.settings.BankAccount
import com.bukuwarung.edc.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreSettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val fakeSettingsRepository: FakeSettingsRepository // Delegate other calls to fake for now
) : SettingsRepository {

    companion object {
        /** Default sandbox merchant account UUID for development/testing. */
        const val DEFAULT_ACCOUNT_ID = "5252d46d-346a-4e67-a9b7-3ab63a8e4a72"
        const val DEFAULT_PHONE_NUMBER = "8299447200"
    }

    private object PreferencesKeys {
        val IS_FIRST_TIME_USER = booleanPreferencesKey("is_first_time_user")
        val PHONE_NUMBER = stringPreferencesKey("phone_number")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val ACCOUNT_ID = stringPreferencesKey("account_id")
    }

    override suspend fun getAccountSettings(): AccountSettings {
        return fakeSettingsRepository.getAccountSettings()
    }

    override suspend fun getBankAccounts(): List<BankAccount> {
        return fakeSettingsRepository.getBankAccounts()
    }

    override fun isFirstTimeUser(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.IS_FIRST_TIME_USER] ?: false
            }
    }

    override suspend fun setIsFirstTimeUser(isFirstTime: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_TIME_USER] = isFirstTime
        }
    }

    override fun getPhoneNumber(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.PHONE_NUMBER] ?: DEFAULT_PHONE_NUMBER
            }
    }

    override suspend fun setPhoneNumber(phoneNumber: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PHONE_NUMBER] = phoneNumber
        }
    }

    override fun getAccessToken(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.ACCESS_TOKEN] ?: ""
            }
    }

    override suspend fun setAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
        }
    }

    override fun getAccountId(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.ACCOUNT_ID] ?: DEFAULT_ACCOUNT_ID
            }
    }

    override suspend fun setAccountId(accountId: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCOUNT_ID] = accountId
        }
    }
}
