package com.bukuwarung.edc.ui.transfer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bukuwarung.edc.ui.R
import com.bukuwarung.edc.ui.common.FlowVariant
import com.bukuwarung.edc.ui.common.transferSelectAccountScreenTitle
import com.bukuwarung.edc.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferSelectAccountScreen(
    variant: FlowVariant = FlowVariant.Transfer,
    onBack: () -> Unit,
    onAccountSelected: (String) -> Unit
) {
    Scaffold(
        containerColor = Colors.White,
        topBar = {
            TopAppBar(
                title = { 
                    Text(stringResource(variant.transferSelectAccountScreenTitle))
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
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.transfer_pilih_akun),
                fontSize = 14.sp,
                color = Colors.TextGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            AccountOption(
                title = stringResource(R.string.transfer_rekening_tabungan),
                icon = Icons.Default.AccountBalanceWallet,
                onClick = { onAccountSelected("TABUNGAN") }
            )

            HorizontalDivider(color = Colors.SurfaceBorder, thickness = 1.dp)

            AccountOption(
                title = stringResource(R.string.transfer_rekening_giro),
                icon = Icons.Default.Description,
                onClick = { onAccountSelected("GIRO") }
            )
        }
    }
}

@Composable
fun AccountOption(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Colors.IconBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Colors.TextGray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransferSelectAccountScreenPreview() {
    TransferSelectAccountScreen(
        variant = FlowVariant.Transfer,
        onBack = {},
        onAccountSelected = {}
    )
}

@Preview(showBackground = true)
@Composable
fun BalanceCheckSelectAccountScreenPreview() {
    TransferSelectAccountScreen(
        variant = FlowVariant.BalanceCheck,
        onBack = {},
        onAccountSelected = {}
    )
}
