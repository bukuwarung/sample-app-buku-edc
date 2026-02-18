package com.bukuwarung.edc.domain.transaction

/**
 * Repository for card-related operations.
 *
 * Partners: This interface abstracts two SDK operations:
 * - [getCardInfo] — reads card details after the cardholder inserts their card
 * - [checkIncompleteTransactions] — detects any pending transactions on app start
 */
interface CardRepository {

    /**
     * Retrieves card information after the cardholder inserts their card.
     *
     * Partners: The SDK does not expose a standalone card-read method. In practice,
     * card data ([CardInfo.cardNumber], [CardInfo.cardHolderName]) is returned as part
     * of [com.bukuwarung.edc.sdk.models.CardReceiptResponse] from `checkBalance()` or
     * `transferInquiry()`. This implementation authenticates the session and returns
     * SANDBOX test card data — replace with real card data from your transaction response.
     *
     * @return [Result] containing [CardInfo] on success, or an SDK exception on failure.
     */
    suspend fun getCardInfo(): Result<CardInfo>

    /**
     * Checks for any incomplete (pending) transactions from a previous session.
     *
     * Partners: Call this on app start. If a non-null result is returned, prompt
     * the user to resume or discard the pending transaction before proceeding.
     *
     * Delegates to `BukuEdcSdk.checkIncompleteTransactions()`.
     *
     * @return [Result] containing [IncompleteTransactionInfo] if a pending transaction
     *         exists, or `null` if there are none.
     */
    suspend fun checkIncompleteTransactions(): Result<IncompleteTransactionInfo?>
}
