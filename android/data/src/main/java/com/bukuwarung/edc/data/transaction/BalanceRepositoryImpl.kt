package com.bukuwarung.edc.data.transaction

import com.bukuwarung.edc.domain.transaction.BalanceInfo
import com.bukuwarung.edc.domain.transaction.BalanceRepository
import com.bukuwarung.edc.sdk.AtmFeatures
import com.bukuwarung.edc.sdk.model.AccountType
import javax.inject.Inject

/**
 * SDK-backed implementation of [BalanceRepository].
 *
 * Partners: This class delegates to [AtmFeatures.checkBalance] to query the account
 * balance via the EDC terminal. The SDK handles card reading, PIN entry, and backend
 * communication automatically — the partner only needs to provide `accountId` and
 * `accountType`.
 *
 * Note (SDK 0.1.3): The `sourceDetails: BankDetails` parameter was removed.
 * The current signature is `checkBalance(accountId: String, accountType: AccountType)`.
 */
class BalanceRepositoryImpl @Inject constructor(
    private val atmFeatures: AtmFeatures
) : BalanceRepository {

    /**
     * Calls [AtmFeatures.checkBalance] and maps the result to [BalanceInfo].
     *
     * Partners: The SDK's `checkBalance()` accepts:
     * - `accountId` — the account identifier
     * - `accountType` — `AccountType.SAVINGS` (default) or `AccountType.CHECKING`
     *
     * SDK exceptions (DeviceSdkException, BackendException) are wrapped in
     * [Result.failure] for the ViewModel to handle.
     */
    override suspend fun checkBalance(
        accountId: String,
        accountType: String
    ): Result<BalanceInfo> = runCatching {
        val sdkAccountType = AccountType.fromString(accountType)

        val receipt = atmFeatures.checkBalance(
            accountId = accountId,
            accountType = sdkAccountType
        ).getOrThrow()

        // Partners: Map the SDK's CardReceiptResponse to the domain BalanceInfo model.
        // Only the fields relevant to balance check are extracted here.
        BalanceInfo(
            totalAmount = receipt.totalAmount,
            cardNumber = receipt.cardNumber,
            cardHolderName = receipt.cardHolderName,
            bankName = receipt.bankName,
            rrn = receipt.rrn,
            accountType = receipt.accountType.name,
            timestamp = receipt.timestamp
        )
    }
}
