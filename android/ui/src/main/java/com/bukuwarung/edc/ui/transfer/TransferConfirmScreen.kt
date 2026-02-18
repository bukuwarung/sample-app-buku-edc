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
    val processingState by viewModel.processingState.collectAsState()

    // Collect one-shot navigation/error events from the ViewModel.
    // NavigateToSuccess fires when SDK emits TransactionComplete.
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is TransferConfirmViewModel.UiEvent.NavigateToSuccess -> onConfirm()
                is TransferConfirmViewModel.UiEvent.ShowError -> {
                    // Error handling UI (snackbar / dialog) will be enhanced in Task 4-6.
                    // For now, navigating to success is kept via the confirm button.
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
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                ConfirmRow(
                    stringResource(R.string.transfer_tipe_transaksi),
                    if (variant == FlowVariant.CashWithdrawal) "Tarik Tunai" else viewModel.type
                )
                ConfirmRow(stringResource(R.string.transfer_bank_tujuan), viewModel.bankName)
                ConfirmRow(stringResource(R.string.transfer_rekening_tujuan), viewModel.accountNo)
                ConfirmRow(stringResource(R.string.transfer_pemilik_rekening), viewModel.accountName)
                ConfirmRow(stringResource(R.string.transfer_nominal), viewModel.amount)
                ConfirmRow(stringResource(R.string.transfer_berita), viewModel.remark)

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
                        onClick = onConfirm,
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
