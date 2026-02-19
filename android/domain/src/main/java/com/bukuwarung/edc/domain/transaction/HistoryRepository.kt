package com.bukuwarung.edc.domain.transaction

/**
 * Repository for retrieving paginated transaction history from the SDK.
 *
 * Partners: The SDK's `AtmFeatures.getTransactionHistory()` returns a paginated list
 * of past transactions. Use the parameters to control pagination (`pageNumber`,
 * `pageSize`) and the `accountId` to identify the account.
 *
 * Pagination: Check [TransactionHistoryInfo.pagination] to determine if more pages
 * are available. If `currentPage < totalPage`, increment `pageNumber` and call again.
 */
interface HistoryRepository {

    /**
     * Retrieves a page of transaction history for the given account.
     *
     * Partners: This delegates to `AtmFeatures.getTransactionHistory(TransactionFilter)`.
     * The SDK communicates with the backend to retrieve transaction records.
     *
     * @param accountId The account identifier (required)
     * @param pageNumber 0-based page number (default 0)
     * @param pageSize Number of items per page (default 20)
     * @return [Result] containing [TransactionHistoryInfo] on success, or an SDK
     *   exception on failure.
     */
    suspend fun getTransactionHistory(
        accountId: String,
        pageNumber: Int = 0,
        pageSize: Int = 20
    ): Result<TransactionHistoryInfo>
}
