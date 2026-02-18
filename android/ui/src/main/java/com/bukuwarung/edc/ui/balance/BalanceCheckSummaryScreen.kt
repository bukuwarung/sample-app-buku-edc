package com.bukuwarung.edc.ui.balance

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bukuwarung.edc.ui.R
import com.bukuwarung.edc.ui.theme.Colors

/**
 * Balance Check Summary screen — displays the account balance from the SDK.
 *
 * Partners: This screen observes [BalanceCheckViewModel.uiState] which is populated
 * from the `AtmFeatures.checkBalance()` SDK call. It handles three states:
 * - **Loading** — balance check in progress (card read + backend query)
 * - **Success** — displays totalAmount, timestamp, and RRN from CardReceiptResponse
 * - **Error** — shows error message with retry option
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceCheckSummaryScreen(
    viewModel: BalanceCheckViewModel,
    onClose: () -> Unit,
    onShowReceipt: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Colors.White,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.common_close))
                    }
                },
                actions = {
                    TextButton(onClick = onClose) {
                        Text(stringResource(R.string.common_tutup), color = Colors.Black)
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is BalanceCheckViewModel.BalanceUiState.Loading -> {
                // Partners: Loading state while the SDK processes the balance check
                // (card read → PIN entry → backend query).
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
                            stringResource(R.string.transfer_sedang_memproses),
                            color = Colors.TextGray
                        )
                    }
                }
            }

            is BalanceCheckViewModel.BalanceUiState.Error -> {
                // Partners: Error state — show the mapped SDK error message with a retry button.
                // DeviceSdkException and BackendException are handled with user-friendly messages.
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
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.retry() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(stringResource(R.string.common_retry), color = Color.White)
                        }
                    }
                }
            }

            is BalanceCheckViewModel.BalanceUiState.Success -> {
                // Partners: Success state — display balance data from CardReceiptResponse.
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(32.dp))

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2196F3)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        stringResource(R.string.balance_check_summary),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Partners: totalAmount from CardReceiptResponse, formatted as Rupiah.
                    Text(
                        state.balanceAmount,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Colors.Black,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Colors.SurfaceBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Partners: timestamp and rrn from CardReceiptResponse.
                            BalanceRow(stringResource(R.string.transfer_label_waktu), state.timestamp)
                            BalanceRow(stringResource(R.string.transfer_label_ref_no), state.refNo)
                            BalanceRow(stringResource(R.string.balance_check_label_saldo), state.balanceAmount)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(onClick = onShowReceipt) {
                        Text(
                            text = stringResource(R.string.balance_check_lihat_detail),
                            color = Color(0xFF1565C0),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { /* Print receipt */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(stringResource(R.string.transfer_cetak_struk), color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun BalanceRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = Colors.TextGray)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
