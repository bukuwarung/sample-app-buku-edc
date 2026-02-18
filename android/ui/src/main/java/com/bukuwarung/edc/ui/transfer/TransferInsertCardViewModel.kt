package com.bukuwarung.edc.ui.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bukuwarung.edc.domain.transaction.TransactionEvent
import com.bukuwarung.edc.domain.transaction.TransactionEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

/**
 * ViewModel for the Insert Card screen.
 *
 * Observes [TransactionEventRepository.transactionEvents] and navigates
 * to the Card Info screen automatically when [TransactionEvent.CardDetected] fires.
 *
 * **Event-driven navigation (SDK active):**
 * When a transaction is running (`checkBalance`, `transferInquiry`, etc.) the SDK
 * emits [TransactionEvent.WaitingForCard] → [TransactionEvent.CardDetected]. This ViewModel
 * catches `CardDetected` and emits [UiEvent.NavigateToCardInfo].
 *
 * **Demo fallback (no active SDK call):**
 * If no `CardDetected` event arrives within [CARD_DETECT_TIMEOUT_MS], the ViewModel
 * automatically navigates forward so the demo flow still works end-to-end before
 * Tasks 4-6 wire up real SDK calls.
 */
@HiltViewModel
class TransferInsertCardViewModel @Inject constructor(
    private val transactionEventRepository: TransactionEventRepository
) : ViewModel() {

    sealed class UiEvent {
        data object NavigateToCardInfo : UiEvent()
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    /** Visual state: which animation/message to show on the Insert Card screen. */
    sealed class CardState {
        data object WaitingForCard : CardState()
        data class CardDetected(val cardType: String) : CardState()
    }

    private val _cardState = MutableStateFlow<CardState>(CardState.WaitingForCard)
    val cardState: StateFlow<CardState> = _cardState.asStateFlow()

    init {
        observeCardEvents()
    }

    private fun observeCardEvents() {
        viewModelScope.launch {
            // Show WaitingForCard as initial state while observing
            transactionEventRepository.transactionEvents
                .filterIsInstance<TransactionEvent.WaitingForCard>()
                .collect { _cardState.value = CardState.WaitingForCard }
        }
        viewModelScope.launch {
            // Wait for CardDetected — with a timeout fallback for demo mode.
            // Partners: Remove `withTimeoutOrNull` once real SDK calls are initiated.
            withTimeoutOrNull(CARD_DETECT_TIMEOUT_MS) {
                transactionEventRepository.transactionEvents
                    .filterIsInstance<TransactionEvent.CardDetected>()
                    .first()
                    .also { event -> _cardState.value = CardState.CardDetected(event.cardType) }
            }
            // Navigate regardless — SDK event or timeout fallback
            _uiEvent.emit(UiEvent.NavigateToCardInfo)
        }
    }

    companion object {
        /** Fallback timeout before auto-navigating in the absence of a real SDK card event. */
        private const val CARD_DETECT_TIMEOUT_MS = 10_000L
    }
}
