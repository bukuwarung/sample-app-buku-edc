package com.bukuwarung.edc.data.card

import com.bukuwarung.edc.data.util.runSuspendCatching
import com.bukuwarung.edc.domain.transaction.CardInfo
import com.bukuwarung.edc.domain.transaction.CardRepository
import com.bukuwarung.edc.domain.transaction.IncompleteTransactionInfo
import com.bukuwarung.edc.sdk.AtmFeatures
import javax.inject.Inject

/**
 * SDK-backed implementation of [CardRepository].
 *
 * Partners: Two operations are demonstrated here:
 * 1. [getCardInfo] — delegates to [AtmFeatures.getCardInfo] to read card details
 *    from the EDC device.
 * 2. [checkIncompleteTransactions] — delegates to [AtmFeatures.checkIncompleteTransactions]
 *    to detect any pending transactions.
 */
class CardRepositoryImpl @Inject constructor(
    private val atmFeatures: AtmFeatures
) : CardRepository {

    /**
     * Retrieves card information from the SDK.
     *
     * Partners: This calls [AtmFeatures.getCardInfo] which reads the card inserted
     * in the EDC device and returns card details (PAN, expiry date).
     *
     * Note: SDK 0.1.3 CardInfo provides `pan` and `expiryDate` only.
     * Cardholder name is not returned by the SDK — set to empty string.
     *
     * @return [CardInfo] mapped from SDK's CardInfo response
     */
    override suspend fun getCardInfo(): Result<CardInfo> = runSuspendCatching {
        val sdkCardInfo = atmFeatures.getCardInfo().getOrThrow()
        CardInfo(
            cardNumber = sdkCardInfo.pan,
            cardHolderName = "", // SDK 0.1.3 does not provide cardholder name
            expiryDate = sdkCardInfo.expiryDate
        )
    }

    /**
     * Delegates to [AtmFeatures.checkIncompleteTransactions].
     *
     * Partners: Call this on app start (e.g., in HomeViewModel). If a non-null
     * [IncompleteTransactionInfo] is returned, show a dialog prompting the user
     * to resume or discard the pending transaction before proceeding.
     */
    override suspend fun checkIncompleteTransactions(): Result<IncompleteTransactionInfo?> =
        runSuspendCatching {
            atmFeatures.checkIncompleteTransactions().getOrThrow()?.let { incomplete ->
                IncompleteTransactionInfo(
                    transactionId = incomplete.transactionId,
                    amount = incomplete.amount,
                    type = incomplete.type
                )
            }
        }
}
