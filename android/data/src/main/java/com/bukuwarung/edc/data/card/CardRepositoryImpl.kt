package com.bukuwarung.edc.data.card

import com.bukuwarung.edc.data.sdk.AuthTokenProvider
import com.bukuwarung.edc.domain.transaction.CardInfo
import com.bukuwarung.edc.domain.transaction.CardRepository
import com.bukuwarung.edc.domain.transaction.IncompleteTransactionInfo
import com.bukuwarung.edc.sdk.BukuEdcSdk
import javax.inject.Inject

/**
 * SDK-backed implementation of [CardRepository].
 *
 * Partners: Two operations are demonstrated here:
 * 1. [getCardInfo] — authenticates with [BukuEdcSdk.signInUserWithToken] and returns
 *    SANDBOX test card data. In production, card details come from
 *    [com.bukuwarung.edc.sdk.models.CardReceiptResponse] after `checkBalance()` or
 *    `transferInquiry()` completes.
 * 2. [checkIncompleteTransactions] — delegates directly to
 *    [BukuEdcSdk.checkIncompleteTransactions] to detect any pending transactions.
 */
class CardRepositoryImpl @Inject constructor(
    private val sdk: BukuEdcSdk,
    private val tokenProvider: AuthTokenProvider
) : CardRepository {

    /**
     * Authenticates the user session and returns card info.
     *
     * Partners: The SDK does not provide a standalone card-read API. This method
     * calls [BukuEdcSdk.signInUserWithToken] to establish an authenticated session
     * (required before any ATM transaction), then returns test card data for the
     * SANDBOX environment.
     *
     * In a real device integration, replace the hardcoded [CardInfo] with data
     * parsed from the [com.bukuwarung.edc.sdk.models.CardReceiptResponse] you
     * receive from the first transaction call.
     *
     * @throws Exception propagated from [tokenProvider] or [BukuEdcSdk.signInUserWithToken]
     */
    override suspend fun getCardInfo(): Result<CardInfo> = runCatching {
        // Step 1: Obtain auth token from your backend (partners replace this lambda)
        val token = tokenProvider.getToken()

        // Step 2: Authenticate the SDK session — required before ATM operations
        // Partners: signInUserWithToken() validates the token with the Buku backend.
        // Use a fresh token from your auth service here.
        sdk.signInUserWithToken(token).getOrThrow()

        // Step 3: Return card data
        // Partners: Replace this with real card fields from CardReceiptResponse
        // (cardNumber, cardHolderName) once a transaction is completed.
        // The SDK returns masked card numbers like "**** **** **** 1537".
        CardInfo(
            cardNumber = "6221 1234 6543 1537",
            cardHolderName = "TEST CARDHOLDER",
            expiryDate = "01/25"
        )
    }

    /**
     * Delegates to [BukuEdcSdk.checkIncompleteTransactions].
     *
     * Partners: Call this on app start (e.g., in HomeViewModel). If a non-null
     * [IncompleteTransactionInfo] is returned, show a dialog prompting the user
     * to resume or discard the pending transaction before proceeding.
     */
    override suspend fun checkIncompleteTransactions(): Result<IncompleteTransactionInfo?> =
        runCatching {
            sdk.checkIncompleteTransactions().getOrThrow()?.let { incomplete ->
                IncompleteTransactionInfo(
                    transactionId = incomplete.transactionId,
                    amount = incomplete.amount,
                    type = incomplete.type
                )
            }
        }
}
