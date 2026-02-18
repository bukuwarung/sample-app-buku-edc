package com.bukuwarung.edc.ui.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bukuwarung.edc.domain.transaction.TransactionEvent
import com.bukuwarung.edc.domain.transaction.TransactionEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the PIN entry screen.
 *
 * Manages manual PIN input from the on-screen keypad AND observes
 * [TransactionEventRepository.transactionEvents] for the SDK's
 * [TransactionEvent.EnteringPin] signal.
 *
 * **SDK integration context:**
 * In a real EDC flow, PIN entry is handled securely inside the SDK (not through this
 * on-screen keypad). When the SDK emits [TransactionEvent.EnteringPin], it means the
 * physical keypad is active. The [isPinPromptActive] flag reflects this for UI display
 * purposes (e.g. showing a "Cardholder is entering PIN" status to the merchant).
 */
@HiltViewModel
class TransferPinViewModel @Inject constructor(
    private val transactionEventRepository: TransactionEventRepository
) : ViewModel() {

    private val _pin = MutableStateFlow("")
    val pin: StateFlow<String> = _pin.asStateFlow()

    /**
     * `true` when the SDK has emitted [TransactionEvent.EnteringPin], indicating the
     * cardholder's PIN entry is in progress on the physical terminal.
     *
     * Partners: Use this to show a "Please wait — cardholder is entering PIN" message
     * and disable any in-app input while the SDK handles PIN entry securely.
     */
    private val _isPinPromptActive = MutableStateFlow(false)
    val isPinPromptActive: StateFlow<Boolean> = _isPinPromptActive.asStateFlow()

    init {
        observePinEvents()
    }

    fun onPinChange(newPin: String) {
        if (newPin.length <= 6) {
            _pin.value = newPin
        }
    }

    private fun observePinEvents() {
        viewModelScope.launch {
            transactionEventRepository.transactionEvents
                .filterIsInstance<TransactionEvent.EnteringPin>()
                .collect {
                    // SDK has signaled that the cardholder is entering their PIN
                    _isPinPromptActive.value = true
                }
        }
    }
}
