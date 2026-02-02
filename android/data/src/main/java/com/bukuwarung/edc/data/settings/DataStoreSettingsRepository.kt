package com.bukuwarung.edc.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
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

    private object PreferencesKeys {
        val IS_FIRST_TIME_USER = booleanPreferencesKey("is_first_time_user")
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
                preferences[PreferencesKeys.IS_FIRST_TIME_USER] ?: true
            }
    }

    override suspend fun setIsFirstTimeUser(isFirstTime: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_TIME_USER] = isFirstTime
        }
    }
}
