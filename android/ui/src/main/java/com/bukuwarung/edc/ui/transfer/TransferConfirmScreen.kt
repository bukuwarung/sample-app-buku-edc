package com.bukuwarung.edc.ui.transfer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bukuwarung.edc.ui.R
import com.bukuwarung.edc.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferConfirmScreen(
    viewModel: TransferConfirmViewModel,
    onBack: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Scaffold(
        containerColor = Colors.White,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.transfer_konfirmasi)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_back))
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
            ConfirmRow(stringResource(R.string.transfer_tipe_transaksi), viewModel.type)
            ConfirmRow(stringResource(R.string.transfer_bank_tujuan), viewModel.bankName)
            ConfirmRow(stringResource(R.string.transfer_rekening_tujuan), viewModel.accountNo)
            ConfirmRow(stringResource(R.string.transfer_pemilik_rekening), viewModel.accountName)
            ConfirmRow(stringResource(R.string.transfer_nominal), viewModel.amount)
            ConfirmRow(stringResource(R.string.transfer_berita), viewModel.remark)
            
            Spacer(modifier = Modifier.weight(1f))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(stringResource(R.string.transfer_action_transfer), color = Color.White)
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
