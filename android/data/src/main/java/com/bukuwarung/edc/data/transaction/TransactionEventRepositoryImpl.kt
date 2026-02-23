package com.bukuwarung.edc.data.transaction

import com.bukuwarung.edc.domain.transaction.TransactionEvent
import com.bukuwarung.edc.sdk.AtmFeatures
import com.bukuwarung.edc.sdk.model.TransactionEvent as SdkTransactionEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import com.bukuwarung.edc.domain.transaction.TransactionEventRepository
import javax.inject.Inject

/**
 * Repository implementation that bridges the BukuEDC SDK's transaction event stream
 * to domain-level [TransactionEvent] objects consumed by ViewModels.
 *
 * **Partner guidance:**
 * - Events are emitted during active SDK operations (`checkBalance`, `transferInquiry`, etc.)
 * - Each `.collect {}` call on [transactionEvents] independently receives events from that
 *   moment forward — multiple screens can observe simultaneously.
 */
class TransactionEventRepositoryImpl @Inject constructor(
    private val atmFeatures: AtmFeatures
) : TransactionEventRepository {

    /**
     * Hot flow of domain transaction events mapped from the SDK's SharedFlow.
     *
     * Events that have no meaningful domain representation (e.g. `BluetoothEvent`,
     * `FirmwareUpdate`, `RequiresUserAction`) are filtered out via [mapNotNull].
     */
    override val transactionEvents: Flow<TransactionEvent> =
        atmFeatures.transactionEvents.mapNotNull { it.toDomainOrNull() }
}

/**
 * Maps a raw SDK [SdkTransactionEvent] to a domain [TransactionEvent].
 *
 * Returns `null` for SDK-internal events not relevant to the partner UI
 * (Bluetooth status, firmware update progress, low-level card reading).
 */
private fun SdkTransactionEvent.toDomainOrNull(): TransactionEvent? = when (this) {
    is SdkTransactionEvent.WaitingForCard ->
        TransactionEvent.WaitingForCard

    is SdkTransactionEvent.CardDetected ->
        TransactionEvent.CardDetected(cardType = cardType)

    is SdkTransactionEvent.EnteringPin ->
        TransactionEvent.EnteringPin

    is SdkTransactionEvent.ProcessingTransaction ->
        TransactionEvent.ProcessingTransaction(step = step)

    is SdkTransactionEvent.TransactionComplete ->
        // Full receipt data (result field) will be mapped in Transfer flow tasks.
        TransactionEvent.TransactionComplete

    is SdkTransactionEvent.TransactionFailed ->
        TransactionEvent.TransactionFailed(
            message = error.message ?: "Transaction failed",
            errorCode = null, // SDK-specific error codes handled in Tasks 4-6
            canRetry = canRetry
        )

    else ->
        // ReadingCard, BluetoothEvent, FirmwareUpdate, RequiresUserAction —
        // Not surfaced to the partner UI in this sample app.
        null
}
