package com.bukuwarung.edc.ui.developer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bukuwarung.edc.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperSettingsScreen(
    viewModel: DeveloperSettingsViewModel,
    onBack: () -> Unit
) {
    val isFirstTimeUser by viewModel.isFirstTimeUser.collectAsState()
    val savedPhoneNumber by viewModel.phoneNumber.collectAsState()
    val savedAccessToken by viewModel.accessToken.collectAsState()

    var phoneNumberInput by remember(savedPhoneNumber) { mutableStateOf(savedPhoneNumber) }
    var accessTokenInput by remember(savedAccessToken) { mutableStateOf(savedAccessToken) }
    var tokenSaved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Developer Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // ————————————————————————————————————————————
            // SDK Authentication Section
            // ————————————————————————————————————————————
            Text(
                text = "SDK AUTHENTICATION",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Colors.TextGray,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                OutlinedTextField(
                    value = phoneNumberInput,
                    onValueChange = { phoneNumberInput = it },
                    label = { Text("Phone Number") },
                    placeholder = { Text("081234567890") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = accessTokenInput,
                    onValueChange = {
                        accessTokenInput = it
                        tokenSaved = false
                    },
                    label = { Text("Access Token") },
                    placeholder = { Text("Paste token from /sdk/v1/token/exchange") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Get token: POST https://api-dev.bukuwarung.com/sdk/v1/token/exchange " +
                            "with partnerId, partnerSecret, partnerUserToken. Valid for 1 hour.",
                    fontSize = 11.sp,
                    color = Colors.TextGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (savedAccessToken.isNotEmpty()) {
                    Text(
                        text = "Current token: ${savedAccessToken.take(20)}...${savedAccessToken.takeLast(10)}",
                        fontSize = 11.sp,
                        color = Color(0xFF2196F3),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        viewModel.setPhoneNumber(phoneNumberInput)
                        viewModel.setAccessToken(accessTokenInput)
                        tokenSaved = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        if (tokenSaved) "Saved ✓" else "Save Credentials",
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Colors.SurfaceBorder)

            // ————————————————————————————————————————————
            // Developer Toggles Section
            // ————————————————————————————————————————————
            Text(
                text = "DEVELOPER TOGGLES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Colors.TextGray,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )

            DeveloperSettingItem(
                title = "Simulate First Time User",
                subtitle = "Toggle whether to show the activation flow",
                checked = isFirstTimeUser,
                onCheckedChange = { viewModel.setFirstTimeUser(it) }
            )
            HorizontalDivider(color = Colors.SurfaceBorder)
        }
    }
}

@Composable
fun DeveloperSettingItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title)
            Text(text = subtitle, color = Colors.TextGray)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
