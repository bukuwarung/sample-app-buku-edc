package com.bukuwarung.edc.domain.transaction

import java.math.BigInteger

/**
 * Repository for the two-step transfer flow.
 *
 * Partners: The SDK transfer process works in two sequential steps:
 *
 * 1. **Inquiry** ([transferInquiry]) — Sends the transfer details to the backend
 *    and receives fee information plus a single-use `transactionToken`.
 *    Display the fees on a confirmation screen before proceeding.
 *
 * 2. **Posting** ([transferPosting]) — Submits the `transactionToken` obtained from
 *    the inquiry step to execute the actual fund transfer. The token is valid for
 *    15 minutes and can only be used once.
 *
 * Both methods delegate to `AtmFeatures.transferInquiry()` and
 * `AtmFeatures.transferPosting()` respectively. See [TransferReceiptInfo] for the
 * response model containing fee breakdown, approval codes, and receipt data.
 */
interface TransferRepository {

    /**
     * Step 1: Transfer inquiry — retrieves fees and a transaction token.
     *
     * Partners: Call this after the user fills in transfer details (destination bank,
     * account number, amount). The returned [TransferReceiptInfo] contains:
     * - Fee breakdown: [TransferReceiptInfo.amount], [TransferReceiptInfo.adminFee],
     *   [TransferReceiptInfo.totalAmount]
     * - A single-use [TransferReceiptInfo.transactionToken] for the posting step
     *
     * The token is valid for **15 minutes** from issuance. If posting is not called
     * within that window, the SDK throws `TokenExpiredException`.
     *
     * @param accountId The source account identifier
     * @param amount Transfer amount in smallest currency unit (e.g. Rupiah, not thousands)
     * @param bankCode Destination bank code (e.g. "MANDIRI", "BCA")
     * @param bankName Destination bank display name (e.g. "Bank Mandiri")
     * @param notes Optional transfer notes / remarks
     * @param isCashWithdrawal `false` for transfers, `true` for cash withdrawals
     *        (SDK reuses the same API for both — see Task 6)
     * @param accountType "SAVINGS" or "CHECKING"
     * @return [Result] containing [TransferReceiptInfo] on success, or an SDK exception
     *         ([DeviceSdkException], [BackendException]) on failure
     */
    suspend fun transferInquiry(
        accountId: String,
        amount: BigInteger,
        bankCode: String,
        bankName: String,
        notes: String,
        isCashWithdrawal: Boolean,
        accountType: String
    ): Result<TransferReceiptInfo>

    /**
     * Step 2: Transfer posting — executes the fund transfer using the inquiry token.
     *
     * Partners: Call this only after the user confirms the transfer on the
     * confirmation screen. The [transactionToken] must be the one returned by
     * [transferInquiry] — it is single-use and expires after 15 minutes.
     *
     * On success, the returned [TransferReceiptInfo] contains final receipt data
     * (RRN, approval code, status). On failure:
     * - `TokenExpiredException` — token exceeded 15-minute validity window
     * - `InvalidTokenException` — token was already used or is malformed
     * - `DeviceSdkException` / `BackendException` — device or backend errors
     *
     * @param accountId The source account identifier (same as inquiry)
     * @param transactionToken Single-use token from [transferInquiry]
     * @return [Result] containing [TransferReceiptInfo] on success
     */
    suspend fun transferPosting(
        accountId: String,
        transactionToken: String
    ): Result<TransferReceiptInfo>
}
