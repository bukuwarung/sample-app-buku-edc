package com.bukuwarung.edc.ui.balance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bukuwarung.edc.ui.R
import com.bukuwarung.edc.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceCheckSummaryScreen(
    viewModel: BalanceCheckViewModel,
    onClose: () -> Unit,
    onShowReceipt: () -> Unit
) {
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
            
            Text(
                viewModel.balanceAmount,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Colors.SurfaceBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    BalanceRow(stringResource(R.string.transfer_label_waktu), viewModel.timestamp)
                    BalanceRow(stringResource(R.string.transfer_label_ref_no), viewModel.refNo)
                    BalanceRow(stringResource(R.string.balance_check_label_saldo), viewModel.balanceAmount)
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

@Preview(showBackground = true)
@Composable
fun BalanceCheckSummaryScreenPreview() {
    BalanceCheckSummaryScreen(
        viewModel = BalanceCheckViewModel(),
        onClose = {},
        onShowReceipt = {}
    )
}
