package com.bukuwarung.edc.ui.transfer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferInsertCardScreen(
    variant: FlowVariant = FlowVariant.Transfer,
    onBack: () -> Unit,
    onCardDetected: () -> Unit
) {
    // Simulate card detection
    LaunchedEffect(Unit) {
        delay(2000)
        onCardDetected()
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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Colors.IconBlue
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
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
    }
}
