package com.bukuwarung.edc.domain.transaction

import java.math.BigInteger
import java.util.Date

/**
 * Domain model representing a balance check result from the SDK.
 *
 * Partners: This model is mapped from the `CardReceiptResponse` returned by
 * `AtmFeatures.checkBalance()`. The key fields for the balance check flow are:
 *
 * - [totalAmount] — the account balance to display on the summary screen
 * - [cardNumber], [cardHolderName], [bankName] — card/bank details for the receipt
 * - [rrn] — Retrieval Reference Number (unique bank reference for disputes)
 * - [accountType] — confirmed account type from the bank response
 * - [timestamp] — server timestamp of the balance inquiry
 */
data class BalanceInfo(
    val totalAmount: BigInteger,
    val cardNumber: String,
    val cardHolderName: String,
    val bankName: String,
    val rrn: String,
    val accountType: String,
    val timestamp: Date
)
