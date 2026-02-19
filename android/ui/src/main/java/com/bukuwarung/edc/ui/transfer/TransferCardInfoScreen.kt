package com.bukuwarung.edc.ui.transfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bukuwarung.edc.ui.R
import com.bukuwarung.edc.ui.common.FlowVariant
import com.bukuwarung.edc.ui.common.transferCardInfoScreenTitle
import com.bukuwarung.edc.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferCardInfoScreen(
    variant: FlowVariant = FlowVariant.Transfer,
    viewModel: TransferCardInfoViewModel,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Colors.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(variant.transferCardInfoScreenTitle))
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
            is CardInfoUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is CardInfoUiState.Success -> {
                CardInfoContent(
                    cardNumber = state.cardNumber,
                    expiryDate = state.expiryDate,
                    padding = padding,
                    onContinue = onContinue
                )
            }

            is CardInfoUiState.Error -> {
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
                    TextButton(onClick = { viewModel.loadCardInfo() }) {
                        Text(stringResource(R.string.common_retry))
                    }
                }
            }
        }
    }
}

@Composable
private fun CardInfoContent(
    cardNumber: String,
    expiryDate: String,
    padding: PaddingValues,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CreditCard,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = Colors.IconBlue
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.transfer_informasi_kartu),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.transfer_informasi_kartu_desc),
            fontSize = 14.sp,
            color = Colors.TextGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                stringResource(R.string.transfer_nomor_kartu),
                fontSize = 12.sp,
                color = Colors.TextGray
            )
            Text(cardNumber, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            if (expiryDate.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    stringResource(R.string.transfer_berlaku_sampai),
                    fontSize = 12.sp,
                    color = Colors.TextGray
                )
                Text(expiryDate, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
            shape = MaterialTheme.shapes.small
        ) {
            Text(stringResource(R.string.transfer_lanjut), color = Color.White)
        }
    }
}
