package com.bukuwarung.edc.domain.settings

data class DeviceInfo(
    val registeredPhoneNumber: String,
    val deviceSerialNumber: String,
    val terminalId: String,
)

data class StoreInfo(
    val name: String,
    val address: String,
)

data class AccountSettings(
    val deviceInfo: DeviceInfo,
    val storeInfo: StoreInfo,
)

data class BankAccount(
    val bankName: String,
    val accountHolderName: String,
    val accountNumber: String,
    val isPrimaryPayoutAccount: Boolean,
)
