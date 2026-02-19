package com.bukuwarung.edc.domain.transaction

import kotlinx.coroutines.flow.Flow

/**
 * Repository for observing real-time transaction lifecycle events from the BukuEDC SDK.
 *
 * **Partner guidance:**
 * Inject this repository into ViewModels and collect [transactionEvents] to update
 * your UI during a live SDK transaction. Events are emitted in-order during a
 * `checkBalance()`, `transferInquiry()`, or `transferPosting()` call.
 *
 * **Usage pattern:**
 * ```kotlin
 * viewModelScope.launch {
 *     transactionEventRepository.transactionEvents.collect { event ->
 *         when (event) {
 *             is TransactionEvent.WaitingForCard -> showWaitingAnimation()
 *             is TransactionEvent.CardDetected   -> updateCardTypeLabel(event.cardType)
 *             is TransactionEvent.EnteringPin    -> showPinPrompt()
 *             is TransactionEvent.ProcessingTransaction -> showLoadingOverlay(event.step)
 *             is TransactionEvent.TransactionComplete   -> navigateToSuccess()
 *             is TransactionEvent.TransactionFailed     -> showError(event.message)
 *         }
 *     }
 * }
 * ```
 */
interface TransactionEventRepository {

    /**
     * Hot flow of transaction lifecycle events emitted by the BukuEDC SDK.
     *
     * Each collector receives events from the moment it begins collecting.
     * Multiple screens can simultaneously collect and each reacts to relevant
     * events independently.
     */
    val transactionEvents: Flow<TransactionEvent>
}
