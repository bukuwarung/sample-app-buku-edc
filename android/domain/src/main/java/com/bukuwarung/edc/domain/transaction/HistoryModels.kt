package com.bukuwarung.edc.domain.transaction

/**
 * Domain model representing a single transaction history entry.
 *
 * Partners: This is mapped from the SDK's `HistoryItem` returned by
 * `AtmFeatures.getTransactionHistory()`. Each field corresponds to an SDK field:
 * - [transactionId] — unique identifier for the transaction
 * - [amount] — transaction amount as a string (formatted by the SDK)
 * - [status] — transaction status (e.g. "SUCCESS", "FAILED", "PENDING")
 * - [date] — transaction date as a string (formatted by the SDK)
 * - [type] — transaction type (e.g. "TRANSFER", "BALANCE_INQUIRY", "CASH_WITHDRAWAL")
 */
data class HistoryItemInfo(
    val transactionId: String,
    val amount: String,
    val status: String,
    val date: String,
    val type: String
)

/**
 * Domain model for pagination details accompanying a transaction history response.
 *
 * Partners: Use these fields to implement infinite-scroll pagination:
 * - If [currentPage] < [totalPage], there are more pages to load
 * - Increment `TransactionFilter.pageNumber` and call `getTransactionHistory()` again
 * - [totalCount] is the total number of transactions matching the filter
 */
data class HistoryPaginationInfo(
    val currentPage: Int,
    val totalPage: Int,
    val totalCount: Int
)

/**
 * Domain model for a paginated transaction history response.
 *
 * Partners: This wraps the SDK's `TransactionHistory` response, containing:
 * - [items] — list of [HistoryItemInfo] entries for the current page
 * - [pagination] — [HistoryPaginationInfo] for navigating between pages
 */
data class TransactionHistoryInfo(
    val items: List<HistoryItemInfo>,
    val pagination: HistoryPaginationInfo
)
