package com.bukuwarung.edc.ui.transfer

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
 * Success screen showing the completed transfer or cash withdrawal receipt.
 *
 * Partners: All data displayed here comes from the SDK's CardReceiptResponse
 * returned by `AtmFeatures.transferPosting()`, mapped through [TransferSuccessViewModel].
 * Key receipt fields: totalAmount, amount, adminFee, rrn, approvalCode, status, timestamp.
 *
 * This screen is reused for both transfers and cash withdrawals — the SDK returns
 * the same CardReceiptResponse format for both since withdrawals use the transfer API
 * with `isCashWithdrawal = true`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferSuccessScreen(
    viewModel: TransferSuccessViewModel,
    onClose: () -> Unit
) {
    Scaffold(
        containerColor = Colors.White,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.clearFlowState()
                        onClose()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.common_close))
                    }
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.clearFlowState()
                        onClose()
                    }) {
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
                stringResource(R.string.transfer_hore_berhasil),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                viewModel.totalAmount,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                stringResource(R.string.transfer_bukti_transaksi),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Colors.TextGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Colors.SurfaceBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SuccessRow(stringResource(R.string.transfer_label_waktu), viewModel.date)
                    SuccessRow(stringResource(R.string.transfer_label_ref_no), viewModel.rrn)
                    SuccessRow(stringResource(R.string.transfer_label_approval_code), viewModel.approvalCode)
                    SuccessRow(stringResource(R.string.transfer_label_status), viewModel.status)
                    SuccessRow(stringResource(R.string.transfer_label_nominal), viewModel.amount)
                    SuccessRow(stringResource(R.string.transfer_label_admin_fee), viewModel.adminFee)
                    SuccessRow(stringResource(R.string.transfer_label_total), viewModel.totalAmount)
                }
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
fun SuccessRow(label: String, value: String) {
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
