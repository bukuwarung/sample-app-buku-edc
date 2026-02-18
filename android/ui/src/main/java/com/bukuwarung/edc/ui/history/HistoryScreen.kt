package com.bukuwarung.edc.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bukuwarung.edc.domain.transaction.HistoryItemInfo
import com.bukuwarung.edc.ui.R
import com.bukuwarung.edc.ui.theme.Colors

/**
 * Transaction History screen — displays a paginated list of past transactions from the SDK.
 *
 * Partners: This screen observes [HistoryViewModel.uiState] which is populated
 * from the `AtmFeatures.getTransactionHistory()` SDK call. It handles four states:
 * - **Loading** — initial data fetch in progress
 * - **Success** — displays list of transactions with infinite-scroll pagination
 * - **Empty** — no transactions found
 * - **Error** — shows error message with retry option
 *
 * Pagination: When the user scrolls near the bottom of the list and more pages are
 * available, [HistoryViewModel.loadNextPage] is called automatically to fetch the
 * next page and append items.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Colors.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.history_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is HistoryViewModel.HistoryUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            stringResource(R.string.history_loading),
                            color = Colors.TextGray
                        )
                    }
                }
            }

            is HistoryViewModel.HistoryUiState.Empty -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            stringResource(R.string.history_empty_title),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(R.string.history_empty_desc),
                            color = Colors.TextGray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            is HistoryViewModel.HistoryUiState.Error -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            stringResource(R.string.transfer_transaksi_gagal),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            state.message,
                            color = Colors.TextGray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.retry() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1565C0)
                            ),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                stringResource(R.string.common_retry),
                                color = Color.White
                            )
                        }
                    }
                }
            }

            is HistoryViewModel.HistoryUiState.Success -> {
                HistoryList(
                    items = state.items,
                    isLoadingMore = state.isLoadingMore,
                    hasMorePages = state.hasMorePages,
                    onLoadMore = { viewModel.loadNextPage() },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

/**
 * Paginated list of transaction history items with infinite-scroll support.
 *
 * Partners: Pagination is triggered automatically when the user scrolls near the
 * bottom of the list. The SDK's `PaginationDetails.currentPage` and `totalPage`
 * determine whether more pages are available.
 */
@Composable
private fun HistoryList(
    items: List<HistoryItemInfo>,
    isLoadingMore: Boolean,
    hasMorePages: Boolean,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // Partners: Trigger loading the next page when scrolled near the bottom.
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 3 && hasMorePages && !isLoadingMore
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize()
    ) {
        items(items, key = { "${it.transactionId}_${it.date}" }) { item ->
            HistoryItemRow(item)
            HorizontalDivider(color = Colors.SurfaceBorder)
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

/**
 * Single transaction history row displaying key fields from [HistoryItemInfo].
 *
 * Partners: Each field corresponds to an SDK `HistoryItem` field:
 * - `type` — transaction type (e.g. "TRANSFER", "BALANCE_INQUIRY", "CASH_WITHDRAWAL")
 * - `amount` — formatted amount string from the SDK
 * - `status` — transaction status (e.g. "SUCCESS", "FAILED", "PENDING")
 * - `date` — formatted date string from the SDK
 * - `transactionId` — unique identifier
 */
@Composable
private fun HistoryItemRow(item: HistoryItemInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Colors.White),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatTransactionType(item.type),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Colors.Black
                )
                Text(
                    text = item.amount,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Colors.Black
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.date,
                    fontSize = 12.sp,
                    color = Colors.TextGray
                )
                StatusBadge(status = item.status)
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "ID: ${item.transactionId}",
                fontSize = 11.sp,
                color = Colors.TextGray
            )
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (backgroundColor, textColor) = when (status.uppercase()) {
        "SUCCESS" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        "FAILED" -> Color(0xFFFFEBEE) to Color(0xFFC62828)
        "PENDING" -> Color(0xFFFFF8E1) to Color(0xFFF57F17)
        else -> Colors.LightGray to Colors.TextGray
    }

    Box(
        modifier = Modifier
            .background(backgroundColor, MaterialTheme.shapes.small)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = status,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

/**
 * Formats SDK transaction type strings to user-friendly labels.
 *
 * Partners: The SDK returns type strings like "TRANSFER", "BALANCE_INQUIRY",
 * "CASH_WITHDRAWAL". Map these to localized labels for your UI.
 */
private fun formatTransactionType(type: String): String = when (type.uppercase()) {
    "TRANSFER" -> "Transfer"
    "BALANCE_INQUIRY" -> "Cek Saldo"
    "CASH_WITHDRAWAL" -> "Tarik Tunai"
    else -> type
}
