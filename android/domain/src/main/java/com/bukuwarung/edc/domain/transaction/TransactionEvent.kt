package com.bukuwarung.edc.domain.transaction

/**
 * Domain representation of SDK transaction lifecycle events.
 *
 * Mapped from `com.bukuwarung.edc.sdk.models.TransactionEvent` in the data layer.
 * ViewModels observe these events via [TransactionEventRepository.transactionEvents]
 * to drive real-time UI state during card transactions.
 *
 * **Typical event sequence:**
 * 1. [WaitingForCard] — displayed on InsertCard screen
 * 2. [CardDetected] — triggers navigation to CardInfo screen
 * 3. [EnteringPin] — displayed on PIN screen
 * 4. [ProcessingTransaction] — displays loading overlay on Confirm screen
 * 5. [TransactionComplete] or [TransactionFailed]
 */
sealed class TransactionEvent {

    /** SDK is waiting for the cardholder to insert or tap their card. */
    data object WaitingForCard : TransactionEvent()

    /**
     * Card has been detected and is being read by the terminal.
     *
     * @property cardType The type of card detected (e.g. "CHIP", "SWIPE", "CONTACTLESS").
     */
    data class CardDetected(val cardType: String) : TransactionEvent()

    /** SDK is prompting the cardholder to enter their ATM PIN. */
    data object EnteringPin : TransactionEvent()

    /**
     * Transaction is being processed on the network.
     *
     * @property step Human-readable description of the current processing step.
     */
    data class ProcessingTransaction(val step: String) : TransactionEvent()

    /**
     * Transaction completed successfully.
     *
     * Note: Full receipt data (amount, RRN, approvalCode, etc.) will be
     * attached here in the Transfer flow tasks.
     */
    data object TransactionComplete : TransactionEvent()

    /**
     * Transaction failed.
     *
     * @property message Human-readable error description.
     * @property errorCode Optional SDK error code (e.g. "INVALID_TOKEN", "TIMEOUT").
     * @property canRetry Whether the partner app should offer a retry option.
     */
    data class TransactionFailed(
        val message: String,
        val errorCode: String? = null,
        val canRetry: Boolean = false
    ) : TransactionEvent()
}
