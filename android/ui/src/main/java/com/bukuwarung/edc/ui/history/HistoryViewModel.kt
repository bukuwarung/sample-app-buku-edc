package com.bukuwarung.edc.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bukuwarung.edc.domain.settings.SettingsRepository
import com.bukuwarung.edc.domain.transaction.HistoryItemInfo
import com.bukuwarung.edc.domain.transaction.HistoryRepository
import com.bukuwarung.edc.sdk.exceptions.BackendException
import com.bukuwarung.edc.sdk.exceptions.DeviceSdkException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Transaction History screen.
 *
 * Partners: This ViewModel demonstrates paginated history retrieval via the SDK:
 *
 * 1. On initialization, it calls [HistoryRepository.getTransactionHistory] with
 *    `TransactionFilter(accountId, pageNumber = 0, pageSize = 20)`.
 *
 * 2. The response contains a list of [HistoryItemInfo] entries and pagination metadata.
 *    If `currentPage < totalPage`, more pages can be loaded via [loadNextPage].
 *
 * 3. Each [HistoryItemInfo] contains: `transactionId`, `amount`, `status`, `date`, `type`
 *    — all as strings formatted by the SDK.
 *
 * Error handling covers SDK exception types:
 * - [DeviceSdkException] — EDC device errors
 * - [BackendException] — backend processing errors
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    // ——————————————————————————————————————————————————————————————
    // UI state
    // ——————————————————————————————————————————————————————————————

    sealed class HistoryUiState {
        /** Initial load in progress — show full-screen loading indicator. */
        data object Loading : HistoryUiState()

        /** History loaded successfully. */
        data class Success(
            val items: List<HistoryItemInfo>,
            val isLoadingMore: Boolean = false,
            val hasMorePages: Boolean = false
        ) : HistoryUiState()

        /** No transactions found — show empty state. */
        data object Empty : HistoryUiState()

        /** Loading failed — show error message with retry option. */
        data class Error(val message: String) : HistoryUiState()
    }

    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    // Partners: Pagination state — tracks the current page and accumulated items.
    private var currentPage = 0
    private var totalPage = 0
    private val allItems = mutableListOf<HistoryItemInfo>()

    companion object {
        private const val PAGE_SIZE = 20
    }

    init {
        loadHistory()
    }

    // ——————————————————————————————————————————————————————————————
    // History loading
    // Partners: Transaction history is loaded automatically when the screen opens.
    // The SDK's getTransactionHistory() accepts a TransactionFilter with pagination
    // parameters and returns TransactionHistory containing HistoryItem entries
    // and PaginationDetails.
    // ——————————————————————————————————————————————————————————————

    private fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = HistoryUiState.Loading
            currentPage = 0
            allItems.clear()

            val accountId = settingsRepository.getAccountId().first().trim()
                .ifEmpty { "no-account-id-configured" }

            // Partners: pageNumber is 0-based, pageSize defaults to 20.
            // The repository constructs the SDK's TransactionFilter internally.
            historyRepository.getTransactionHistory(
                accountId = accountId,
                pageNumber = currentPage,
                pageSize = PAGE_SIZE
            )
                .onSuccess { result ->
                    currentPage = result.pagination.currentPage
                    totalPage = result.pagination.totalPage
                    allItems.addAll(result.items)

                    if (allItems.isEmpty()) {
                        _uiState.value = HistoryUiState.Empty
                    } else {
                        _uiState.value = HistoryUiState.Success(
                            items = allItems.toList(),
                            hasMorePages = currentPage < totalPage
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.value = HistoryUiState.Error(
                        message = getErrorMessage(error)
                    )
                }
        }
    }

    /**
     * Loads the next page of transaction history.
     *
     * Partners: Call this when the user scrolls to the end of the list.
     * If `currentPage < totalPage`, it increments `pageNumber` and appends
     * new items to the existing list.
     */
    fun loadNextPage() {
        val current = _uiState.value
        if (current !is HistoryUiState.Success || current.isLoadingMore || !current.hasMorePages) return

        viewModelScope.launch {
            _uiState.value = current.copy(isLoadingMore = true)

            val accountId = settingsRepository.getAccountId().first().trim()
                .ifEmpty { "no-account-id-configured" }

            historyRepository.getTransactionHistory(
                accountId = accountId,
                pageNumber = currentPage + 1,
                pageSize = PAGE_SIZE
            )
                .onSuccess { result ->
                    currentPage = result.pagination.currentPage
                    totalPage = result.pagination.totalPage
                    allItems.addAll(result.items)

                    _uiState.value = HistoryUiState.Success(
                        items = allItems.toList(),
                        hasMorePages = currentPage < totalPage
                    )
                }
                .onFailure {
                    // Partners: If loading more pages fails, keep existing items visible
                    // and stop the loading indicator. The user can scroll down again to retry.
                    _uiState.value = current.copy(isLoadingMore = false)
                }
        }
    }

    /** Retries the initial history load (e.g. after a transient error). */
    fun retry() {
        loadHistory()
    }

    /**
     * Maps SDK exceptions to user-facing error messages.
     *
     * Partners: The SDK throws specific exception types for different failure modes.
     * Handle each type to provide actionable guidance to the user.
     */
    private fun getErrorMessage(error: Throwable): String = when (error) {
        is DeviceSdkException ->
            "Kesalahan perangkat (${error.code}): ${error.message}"
        is BackendException ->
            "Kesalahan server (${error.code}): ${error.message}"
        else ->
            error.message ?: "Terjadi kesalahan. Silakan coba lagi."
    }
}
