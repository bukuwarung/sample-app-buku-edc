package com.bukuwarung.edc.domain.transaction

import java.math.BigInteger
import java.util.Date

/**
 * Domain model representing a transfer receipt from the SDK.
 *
 * Partners: This model is mapped from `CardReceiptResponse` returned by both
 * `AtmFeatures.transferInquiry()` (step 1) and `AtmFeatures.transferPosting()` (step 2).
 *
 * After **inquiry**, the key fields are:
 * - [amount], [adminFee], [totalAmount] — fee breakdown to display on confirmation screen
 * - [transactionToken] — single-use token needed for the posting step (valid 15 min)
 *
 * After **posting**, the key fields are:
 * - [rrn], [approvalCode] — receipt identifiers for the completed transaction
 * - [status] — final transaction status ("SUCCESS", "FAILED", etc.)
 */
data class TransferReceiptInfo(
    val transactionId: String,
    val amount: BigInteger,
    val adminFee: BigInteger,
    val totalAmount: BigInteger,
    val cardNumber: String,
    val cardHolderName: String,
    val bankName: String,
    val approvalCode: String,
    val rrn: String,
    val timestamp: Date,
    val status: String,
    val transactionToken: String?,
    val receiptData: String?
)
