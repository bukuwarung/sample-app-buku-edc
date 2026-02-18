package com.bukuwarung.edc.data.transaction

import com.bukuwarung.edc.domain.transaction.TransferReceiptInfo
import com.bukuwarung.edc.domain.transaction.TransferRepository
import com.bukuwarung.edc.sdk.AtmFeatures
import com.bukuwarung.edc.sdk.model.AccountType
import com.bukuwarung.edc.sdk.model.BankDetails
import com.bukuwarung.edc.sdk.model.CardReceiptResponse
import java.math.BigInteger
import javax.inject.Inject

/**
 * SDK-backed implementation of [TransferRepository].
 *
 * Partners: This class demonstrates the two-step transfer pattern:
 *
 * 1. [transferInquiry] → calls [AtmFeatures.transferInquiry] with destination details.
 *    Returns fee breakdown + a single-use `transactionToken`.
 *
 * 2. [transferPosting] → calls [AtmFeatures.transferPosting] with the token from step 1.
 *    Returns final receipt data (RRN, approval code, status).
 *
 * Both methods use `runCatching` so SDK exceptions (DeviceSdkException, BackendException,
 * TokenExpiredException, InvalidTokenException) are wrapped in [Result.failure] for
 * the ViewModel to handle.
 */
class TransferRepositoryImpl @Inject constructor(
    private val atmFeatures: AtmFeatures
) : TransferRepository {

    /**
     * Step 1: Calls [AtmFeatures.transferInquiry] to retrieve fee information
     * and a transaction token.
     *
     * Partners: The SDK accepts [BankDetails] (bankCode + bankName) for the destination.
     * The [isCashWithdrawal] flag differentiates transfers from cash withdrawals —
     * the SDK uses the same API endpoint for both (see Task 6 for withdrawal usage).
     */
    override suspend fun transferInquiry(
        accountId: String,
        amount: BigInteger,
        bankCode: String,
        bankName: String,
        notes: String,
        isCashWithdrawal: Boolean,
        accountType: String
    ): Result<TransferReceiptInfo> = runCatching {
        // Partners: Construct BankDetails from the user's bank selection.
        // The SDK requires both bankCode and bankName.
        val destinationDetails = BankDetails(
            bankCode = bankCode,
            bankName = bankName
        )

        val receipt = atmFeatures.transferInquiry(
            accountId = accountId,
            amount = amount,
            destinationDetails = destinationDetails,
            notes = notes,
            isCashWithdrawal = isCashWithdrawal,
            accountType = AccountType.fromString(accountType)
        ).getOrThrow()

        receipt.toDomain()
    }

    /**
     * Step 2: Calls [AtmFeatures.transferPosting] to execute the transfer.
     *
     * Partners: The [transactionToken] must be the token returned by [transferInquiry].
     * It is single-use and valid for 15 minutes. After that, the SDK throws
     * `TokenExpiredException` and the user must redo the inquiry step.
     */
    override suspend fun transferPosting(
        accountId: String,
        transactionToken: String
    ): Result<TransferReceiptInfo> = runCatching {
        val receipt = atmFeatures.transferPosting(
            accountId = accountId,
            transactionToken = transactionToken
        ).getOrThrow()

        receipt.toDomain()
    }
}

/**
 * Maps the SDK's [CardReceiptResponse] to the domain [TransferReceiptInfo].
 *
 * Partners: The SDK returns a single [CardReceiptResponse] for all transaction types.
 * This mapper extracts only the fields relevant to the transfer flow.
 */
private fun CardReceiptResponse.toDomain(): TransferReceiptInfo = TransferReceiptInfo(
    transactionId = transactionId,
    amount = amount,
    adminFee = adminFee,
    totalAmount = totalAmount,
    cardNumber = cardNumber,
    cardHolderName = cardHolderName,
    bankName = bankName,
    approvalCode = approvalCode,
    rrn = rrn,
    timestamp = timestamp,
    status = status.name,
    transactionToken = transactionToken,
    receiptData = receiptData
)
