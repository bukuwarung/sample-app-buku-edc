package com.bukuwarung.edc.domain.transaction

import java.math.BigInteger

/**
 * Domain model for card information read from a physical card.
 *
 * Partners: In production, [cardNumber] and [cardHolderName] come from the
 * [CardReceiptResponse] returned by `AtmFeatures.checkBalance()` or
 * `AtmFeatures.transferInquiry()`. The SDK does not expose a standalone card-read
 * operation; card data is a byproduct of initiating a transaction.
 */
data class CardInfo(
    /** Masked card number, e.g. "**** **** **** 1537" */
    val cardNumber: String,
    /** Cardholder name as printed on the card */
    val cardHolderName: String,
    /**
     * Card expiry date (MM/YY).
     * Not returned by the SDK — may be null in real integrations.
     */
    val expiryDate: String? = null
)

/**
 * Domain model for an incomplete (pending) transaction detected at app start.
 *
 * Partners: Populated from `BukuEdcSdk.checkIncompleteTransactions()`.
 * Use this to prompt the user to resume or discard the pending transaction.
 */
data class IncompleteTransactionInfo(
    /** Unique transaction identifier */
    val transactionId: String,
    /** Transaction amount in smallest currency unit */
    val amount: BigInteger,
    /** Transaction type: "BALANCE", "TRANSFER", or "WITHDRAWAL" */
    val type: String
)
