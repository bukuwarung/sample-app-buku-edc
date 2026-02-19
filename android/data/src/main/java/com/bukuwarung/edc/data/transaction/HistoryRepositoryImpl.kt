package com.bukuwarung.edc.data.transaction

import com.bukuwarung.edc.data.util.runSuspendCatching
import com.bukuwarung.edc.domain.transaction.HistoryItemInfo
import com.bukuwarung.edc.domain.transaction.HistoryPaginationInfo
import com.bukuwarung.edc.domain.transaction.HistoryRepository
import com.bukuwarung.edc.domain.transaction.TransactionHistoryInfo
import com.bukuwarung.edc.sdk.AtmFeatures
import com.bukuwarung.edc.sdk.model.TransactionFilter
import javax.inject.Inject

/**
 * SDK-backed implementation of [HistoryRepository].
 *
 * Partners: This class delegates to [AtmFeatures.getTransactionHistory] to retrieve
 * paginated transaction records from the backend. The SDK handles authentication
 * and network communication — the partner only needs to provide an account ID and
 * pagination parameters.
 *
 * Each `HistoryItem` from the SDK has nullable String fields; this implementation
 * maps them to non-null [HistoryItemInfo] fields with empty-string defaults.
 */
class HistoryRepositoryImpl @Inject constructor(
    private val atmFeatures: AtmFeatures
) : HistoryRepository {

    /**
     * Calls [AtmFeatures.getTransactionHistory] and maps the result to domain models.
     *
     * Partners: The SDK's `TransactionFilter` accepts additional optional fields
     * (`type`, `status`, `order`, `startDate`, `endDate`, `terminalId`) that can be
     * used for more advanced filtering. This sample uses only pagination parameters.
     *
     * The SDK returns `TransactionHistory` containing:
     * - `history: ArrayList<HistoryItem>` — each item has nullable `transactionId`,
     *   `amount`, `status`, `date`, `type` fields
     * - `paginationDetails: PaginationDetails?` — `currentPage`, `totalPage`, `totalCount`
     *
     * SDK exceptions (DeviceSdkException, BackendException) are wrapped in
     * [Result.failure] for the ViewModel to handle.
     */
    override suspend fun getTransactionHistory(
        accountId: String,
        pageNumber: Int,
        pageSize: Int
    ): Result<TransactionHistoryInfo> = runSuspendCatching {
        // Partners: TransactionFilter is the SDK model that controls pagination and filtering.
        // pageNumber is 0-based, pageSize defaults to 20, order defaults to "DESC".
        val filter = TransactionFilter(
            accountId = accountId,
            pageNumber = pageNumber,
            pageSize = pageSize
        )

        val sdkResult = atmFeatures.getTransactionHistory(filter).getOrThrow()

        // Partners: Map SDK's HistoryItem (nullable fields) to domain HistoryItemInfo (non-null).
        val items = sdkResult.history.map { item ->
            HistoryItemInfo(
                transactionId = item.transactionId.orEmpty(),
                amount = item.amount.orEmpty(),
                status = item.status.orEmpty(),
                date = item.date.orEmpty(),
                type = item.type.orEmpty()
            )
        }

        // Partners: Map SDK's PaginationDetails to domain HistoryPaginationInfo.
        // If paginationDetails is null, default to page 0 of 0.
        val pagination = sdkResult.paginationDetails?.let {
            HistoryPaginationInfo(
                currentPage = it.currentPage,
                totalPage = it.totalPage,
                totalCount = it.totalCount
            )
        } ?: HistoryPaginationInfo(currentPage = 0, totalPage = 0, totalCount = 0)

        TransactionHistoryInfo(items = items, pagination = pagination)
    }
}
