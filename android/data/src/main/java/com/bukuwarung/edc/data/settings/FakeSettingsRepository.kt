package com.bukuwarung.edc.data.settings

import com.bukuwarung.edc.domain.settings.AccountSettings
import com.bukuwarung.edc.domain.settings.BankAccount
import com.bukuwarung.edc.domain.settings.DeviceInfo
import com.bukuwarung.edc.domain.settings.SettingsRepository
import com.bukuwarung.edc.domain.settings.StoreInfo
import com.bukuwarung.edc.sdk.BukuEdcSdk
import javax.inject.Inject

class FakeSettingsRepository @Inject constructor() : SettingsRepository {
    override suspend fun getAccountSettings(): AccountSettings {
        BukuEdcSdk.create()
        BukuEdcSdk.setDebugMode(true)
        return AccountSettings(
            deviceInfo = DeviceInfo(
                registeredPhoneNumber = "0813 1010 1111",
                deviceSerialNumber = "C38753489",
                terminalId = "T73648723",
            ),
            storeInfo = StoreInfo(
                name = "Toko Cahaya Abadi",
                address = "Jl. Pahlawan Revolusi No.24, Jakarta Timur",
            ),
        )
    }

    override suspend fun getBankAccounts(): List<BankAccount> {
        return listOf(
            BankAccount(
                bankName = "BSI",
                accountHolderName = "Febriana",
                accountNumber = "013249322434",
                isPrimaryPayoutAccount = true,
            ),
        )
    }
}
