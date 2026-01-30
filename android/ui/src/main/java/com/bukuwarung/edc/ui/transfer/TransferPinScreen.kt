package com.bukuwarung.edc.ui.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bukuwarung.edc.ui.R
import com.bukuwarung.edc.ui.common.FlowVariant
import com.bukuwarung.edc.ui.common.transferPinScreenTitle
import com.bukuwarung.edc.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferPinScreen(
    variant: FlowVariant = FlowVariant.Transfer,
    viewModel: TransferPinViewModel,
    onBack: () -> Unit,
    onPinEntered: (String) -> Unit
) {
    val pin by viewModel.pin.collectAsState()

    Scaffold(
        containerColor = Colors.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(variant.transferPinScreenTitle))
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
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Warning box
                Surface(
                    color = Color(0xFFFFF9C4),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFFBC02D))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                stringResource(R.string.transfer_pin_warning_title),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                stringResource(R.string.transfer_pin_warning_desc),
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Text(stringResource(R.string.transfer_masukkan_pin_atm), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(24.dp))

                // PIN dots
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    repeat(6) { index ->
                        val isFilled = index < pin.length
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(if (isFilled) Colors.IconBlue else Color.LightGray)
                        )
                    }
                }
            }

            // Keypad
            NumericKeypad(
                onNumberClick = { num -> viewModel.onPinChange(pin + num) },
                onDeleteClick = { if (pin.isNotEmpty()) viewModel.onPinChange(pin.dropLast(1)) },
                onDoneClick = { if (pin.length >= 4) onPinEntered(pin) }
            )
        }
    }
}

@Composable
fun NumericKeypad(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onDoneClick: () -> Unit
) {
    val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "delete", "0", "done")
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        items(keys) { key ->
            KeypadButton(
                key = key,
                onClick = {
                    when (key) {
                        "delete" -> onDeleteClick()
                        "done" -> onDoneClick()
                        else -> onNumberClick(key)
                    }
                }
            )
        }
    }
}

@Composable
fun KeypadButton(key: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(64.dp)
            .clickable { onClick() }
            .background(
                when (key) {
                    "delete" -> Color(0xFFD32F2F)
                    "done" -> Color(0xFF00796B)
                    else -> Color.White
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        when (key) {
            "delete" -> Icon(Icons.Default.Backspace, contentDescription = stringResource(R.string.transfer_keypad_delete), tint = Color.White)
            "done" -> Icon(Icons.AutoMirrored.Filled.KeyboardReturn, contentDescription = stringResource(R.string.transfer_keypad_done), tint = Color.White)
            else -> Text(key, fontSize = 24.sp, fontWeight = FontWeight.Medium, color = Colors.Black)
        }
    }
}
