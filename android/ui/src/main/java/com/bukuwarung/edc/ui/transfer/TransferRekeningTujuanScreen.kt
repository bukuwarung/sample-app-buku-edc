package com.bukuwarung.edc.ui.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bukuwarung.edc.ui.R
import com.bukuwarung.edc.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferRekeningTujuanScreen(
    viewModel: TransferRekeningTujuanViewModel,
    bankName: String,
    onBack: () -> Unit,
    onContinue: (String, String, String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val accountNumber by viewModel.accountNumber.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val remark by viewModel.remark.collectAsState()

    Scaffold(
        containerColor = Colors.White,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.transfer_rekening_tujuan)) },
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
            Text(stringResource(R.string.transfer_bank_tujuan), fontSize = 14.sp, color = Colors.TextGray)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE3F2FD), shape = MaterialTheme.shapes.small)
                    .padding(16.dp)
            ) {
                Text(bankName, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(stringResource(R.string.transfer_nomor_rekening), fontSize = 14.sp, color = Colors.TextGray)
            OutlinedTextField(
                value = accountNumber,
                onValueChange = viewModel::onAccountNumberChange,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(stringResource(R.string.transfer_nominal_transfer), fontSize = 14.sp, color = Colors.TextGray)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ArrowOutward, contentDescription = null, tint = Colors.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Rp", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                TextField(
                    value = amount,
                    onValueChange = viewModel::onAmountChange,
                    modifier = Modifier.weight(1f),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
            }
            HorizontalDivider(color = Colors.Black, thickness = 1.dp)

            Spacer(modifier = Modifier.height(24.dp))

            Text(stringResource(R.string.transfer_berita_opsional), fontSize = 14.sp, color = Colors.TextGray)
            OutlinedTextField(
                value = remark,
                onValueChange = viewModel::onRemarkChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.transfer_masukkan_berita)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onContinue(accountNumber, amount, remark) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                shape = MaterialTheme.shapes.small,
                enabled = accountNumber.isNotBlank() && amount.isNotBlank()
            ) {
                Text(stringResource(R.string.transfer_lanjut), color = Color.White)
            }
        }
    }
}
