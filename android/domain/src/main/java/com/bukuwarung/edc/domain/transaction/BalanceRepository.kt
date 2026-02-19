package com.bukuwarung.edc.domain.transaction

/**
 * Repository for the balance check operation.
 *
 * Partners: The SDK's `AtmFeatures.checkBalance()` queries the account balance
 * via the EDC terminal. It accepts an `accountId` and `accountType` (SAVINGS or
 * CHECKING) and returns a `CardReceiptResponse` containing the balance amount,
 * card details, and receipt identifiers.
 *
 * Note (SDK 0.1.3): The `sourceDetails: BankDetails` parameter was removed in
 * SDK 0.1.3. The method now takes only `accountId` and `accountType`.
 */
interface BalanceRepository {

    /**
     * Performs a balance inquiry on the given account.
     *
     * Partners: This delegates to `AtmFeatures.checkBalance(accountId, accountType)`.
     * The SDK communicates with the EDC device to read the card and query the balance.
     *
     * On success, the returned [BalanceInfo] contains:
     * - [BalanceInfo.totalAmount] — the account balance
     * - [BalanceInfo.cardNumber] — masked card number
     * - [BalanceInfo.cardHolderName] — cardholder name from the bank
     * - [BalanceInfo.bankName] — issuing bank name
     * - [BalanceInfo.rrn] — Retrieval Reference Number for dispute resolution
     * - [BalanceInfo.accountType] — confirmed account type
     *
     * On failure, the SDK throws:
     * - `DeviceSdkException` — device errors (E01 card read, E02 card removed, E06 PIN cancelled)
     * - `BackendException` — backend errors (30 format error, 55 invalid PIN, 03 invalid merchant)
     *
     * @param accountId The account identifier
     * @param accountType "SAVINGS" or "CHECKING"
     * @return [Result] containing [BalanceInfo] on success, or an SDK exception on failure
     */
    suspend fun checkBalance(
        accountId: String,
        accountType: String
    ): Result<BalanceInfo>
}
