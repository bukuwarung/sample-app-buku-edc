package com.bukuwarung.edc.ui.transfer

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bukuwarung.edc.ui.R
import com.bukuwarung.edc.ui.common.FlowVariant
import com.bukuwarung.edc.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferConfirmScreen(
    variant: FlowVariant = FlowVariant.Transfer,
    viewModel: TransferConfirmViewModel,
    onBack: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val processingState by viewModel.processingState.collectAsState()

    // Collect one-shot navigation/error events from the ViewModel.
    // NavigateToSuccess fires after transferPosting() completes successfully.
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is TransferConfirmViewModel.UiEvent.NavigateToSuccess -> onConfirm()
                is TransferConfirmViewModel.UiEvent.ShowError -> {
                    // Partners: Token errors (isTokenError=true) indicate the user
                    // must navigate back and restart the flow. Other errors may allow retry.
                    if (event.isTokenError) {
                        onBack()
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Colors.White,
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.transfer_konfirmasi)) },
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
                is TransferConfirmViewModel.ConfirmUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is TransferConfirmViewModel.ConfirmUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (state.isTokenError) {
                            TextButton(onClick = onBack) {
                                Text(stringResource(R.string.common_back))
                            }
                        } else {
                            TextButton(onClick = { viewModel.retryInquiry() }) {
                                Text(stringResource(R.string.common_retry))
                            }
                        }
                    }
                }

                is TransferConfirmViewModel.ConfirmUiState.InquirySuccess -> {
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Partners: The transaction type label ("Transfer" or "Tarik Tunai")
                        // is determined by the ViewModel based on flowState.isCashWithdrawal.
                        ConfirmRow(
                            stringResource(R.string.transfer_tipe_transaksi),
                            state.type
                        )
                        ConfirmRow(stringResource(R.string.transfer_bank_tujuan), state.bankName)
                        ConfirmRow(stringResource(R.string.transfer_rekening_tujuan), state.accountNo)
                        ConfirmRow(stringResource(R.string.transfer_pemilik_rekening), state.accountName)
                        ConfirmRow(stringResource(R.string.transfer_nominal), state.amount)
                        ConfirmRow(stringResource(R.string.transfer_admin_fee), state.adminFee)
                        ConfirmRow(stringResource(R.string.transfer_total), state.totalAmount)
                        ConfirmRow(stringResource(R.string.transfer_berita), state.remark)

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedButton(
                                onClick = onCancel,
                                modifier = Modifier.weight(1f),
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(stringResource(R.string.transfer_batal))
                            }
                            Button(
                                onClick = { viewModel.confirmTransfer() },
                                modifier = Modifier.weight(1f),
                                enabled = !processingState.isProcessing,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    stringResource(
                                        if (variant == FlowVariant.CashWithdrawal) R.string.home_action_tarik_tunai
                                        else R.string.transfer_action_transfer
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // Processing overlay — shown while SDK is processing the transaction.
        // Triggered by TransactionEvent.ProcessingTransaction from the SDK.
        if (processingState.isProcessing) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.55f)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(56.dp),
                        color = Color.White,
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(R.string.transfer_sedang_memproses),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (processingState.currentStep.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = processingState.currentStep,
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = Colors.TextGray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Colors.Black)
    }
}
