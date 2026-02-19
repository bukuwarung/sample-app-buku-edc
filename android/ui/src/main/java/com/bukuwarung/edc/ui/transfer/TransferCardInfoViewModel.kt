package com.bukuwarung.edc.ui.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bukuwarung.edc.domain.transaction.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the card info screen.
 *
 * Partners: The screen transitions through Loading → Success (or Error) when
 * [CardRepository.getCardInfo] completes. Handle the Error state to show
 * retry or back-navigation options.
 */
sealed class CardInfoUiState {
    data object Loading : CardInfoUiState()
    data class Success(val cardNumber: String, val expiryDate: String) : CardInfoUiState()
    data class Error(val message: String) : CardInfoUiState()
}

/**
 * ViewModel for the card info screen, shared by Transfer, Balance Check,
 * and Cash Withdrawal flows.
 *
 * Partners: On init, [CardRepository.getCardInfo] is called to authenticate
 * the session and retrieve card details. Replace the repository implementation
 * to supply real card data from your transaction response.
 */
@HiltViewModel
class TransferCardInfoViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CardInfoUiState>(CardInfoUiState.Loading)
    val uiState: StateFlow<CardInfoUiState> = _uiState.asStateFlow()

    init {
        loadCardInfo()
    }

    /**
     * Fetches card info from the repository. Called automatically on ViewModel creation.
     *
     * Partners: This triggers [CardRepository.getCardInfo], which delegates to
     * `AtmFeatures.getCardInfo()`. In SANDBOX with `testingMock=true`, the SDK
     * returns test card data. In production, real card data is read from the terminal.
     */
    fun loadCardInfo() {
        viewModelScope.launch {
            _uiState.value = CardInfoUiState.Loading
            cardRepository.getCardInfo()
                .onSuccess { cardInfo ->
                    _uiState.value = CardInfoUiState.Success(
                        cardNumber = cardInfo.cardNumber,
                        expiryDate = cardInfo.expiryDate ?: ""
                    )
                }
                .onFailure { error ->
                    _uiState.value = CardInfoUiState.Error(
                        message = error.message ?: "Failed to read card info"
                    )
                }
        }
    }
}
