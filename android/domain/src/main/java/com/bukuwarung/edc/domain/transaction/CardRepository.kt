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
     * Partners: The SDK exposes `AtmFeatures.getCardInfo()` which reads card details
     * from the terminal. This implementation delegates to that SDK method and maps
     * the response to the domain [CardInfo] model.
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
     * Delegates to `AtmFeatures.checkIncompleteTransactions()`.
     *
     * @return [Result] containing [IncompleteTransactionInfo] if a pending transaction
     *         exists, or `null` if there are none.
     */
    suspend fun checkIncompleteTransactions(): Result<IncompleteTransactionInfo?>
}
