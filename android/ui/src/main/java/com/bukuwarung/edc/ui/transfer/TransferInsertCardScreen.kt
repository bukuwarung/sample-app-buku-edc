package com.bukuwarung.edc.ui.transfer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bukuwarung.edc.ui.R
import com.bukuwarung.edc.ui.common.FlowVariant
import com.bukuwarung.edc.ui.common.transferInsertCardScreenTitle
import com.bukuwarung.edc.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferInsertCardScreen(
    variant: FlowVariant = FlowVariant.Transfer,
    viewModel: TransferInsertCardViewModel,
    onBack: () -> Unit,
    onCardDetected: () -> Unit
) {
    val cardState by viewModel.cardState.collectAsState()

    // Collect one-shot navigation events from the ViewModel.
    // The ViewModel fires NavigateToCardInfo when CardDetected fires (or after a timeout).
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is TransferInsertCardViewModel.UiEvent.NavigateToCardInfo -> onCardDetected()
            }
        }
    }

    Scaffold(
        containerColor = Colors.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(variant.transferInsertCardScreenTitle))
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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon changes when the card is detected
            val isDetected = cardState is TransferInsertCardViewModel.CardState.CardDetected
            Icon(
                imageVector = if (isDetected) Icons.Default.CheckCircle else Icons.Default.CreditCard,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = if (isDetected) Colors.PrimaryGreen else Colors.IconBlue
            )

            Spacer(modifier = Modifier.height(32.dp))

            when (val state = cardState) {
                is TransferInsertCardViewModel.CardState.WaitingForCard -> {
                    Text(
                        text = stringResource(R.string.transfer_masukkan_atau_gesek),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.transfer_masukkan_atau_gesek_desc),
                        fontSize = 14.sp,
                        color = Colors.TextGray,
                        textAlign = TextAlign.Center
                    )
                }

                is TransferInsertCardViewModel.CardState.CardDetected -> {
                    Text(
                        text = stringResource(R.string.transfer_kartu_terdeteksi),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Colors.PrimaryGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.cardType,
                        fontSize = 14.sp,
                        color = Colors.TextGray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Loading indicator while waiting for SDK card events
            if (!isDetected) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = Colors.IconBlue,
                    strokeWidth = 3.dp
                )
            }
        }
    }
}
