package com.bukuwarung.edc.ui.balance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.bukuwarung.edc.ui.theme.Colors

/**
 * Balance Check Receipt screen — detailed receipt view with all SDK data.
 *
 * Partners: This screen reads from [BalanceCheckViewModel.uiState], which was populated
 * by the `AtmFeatures.checkBalance()` call on the Summary screen. The receipt displays
 * all relevant fields from `CardReceiptResponse`:
 * - timestamp, rrn (Ref No.) — transaction identifiers
 * - accountType, cardNumber — card/account details
 * - totalAmount — the account balance
 * - cardHolderName, bankName — cardholder and bank info
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceCheckReceiptScreen(
    viewModel: BalanceCheckViewModel,
    onClose: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
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
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        // Partners: The receipt screen only renders when the balance check was successful.
        // If the state is not Success (e.g., navigated here without data), show fallback "-".
        val state = uiState as? BalanceCheckViewModel.BalanceUiState.Success

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Partners: cardHolderName and bankName from CardReceiptResponse.
                    Text(
                        text = state?.cardHolderName ?: "-",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = state?.bankName ?: "-",
                        fontSize = 12.sp,
                        color = Colors.TextGray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Partners: Transaction identifiers from CardReceiptResponse.
                    ReceiptRow("Waktu", state?.timestamp ?: "-")
                    ReceiptRow("Ref No.", state?.refNo ?: "-")

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray)

                    // Partners: Card/account details from CardReceiptResponse.
                    ReceiptRow("Jenis Kartu", state?.accountType ?: "-")
                    ReceiptRow("Nomor Kartu", state?.cardNumber ?: "-")

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray)

                    // Partners: totalAmount from CardReceiptResponse — the account balance.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Saldo", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(state?.balanceAmount ?: "-", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        "SIMPAN RESI INI SEBAGAI\nBUKTI TRANSAKSI YANG SAH",
                        fontSize = 10.sp,
                        color = Colors.TextGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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
fun ReceiptRow(label: String, value: String) {
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
