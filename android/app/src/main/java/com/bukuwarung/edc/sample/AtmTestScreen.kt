package com.bukuwarung.edc.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bukuwarung.edc.sdk.AtmFeatures
import com.bukuwarung.edc.sdk.model.AccountType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AtmTestScreen() {
    val context = LocalContext.current
    val sdk = (context.applicationContext as BukuEdcApplication).sdk
    val scope = rememberCoroutineScope()

    var accessToken by remember { mutableStateOf("eyJhbGciOiJSUzI1NiIsImtpZCI6ImY1MzMwMzNhMTMzYWQyM2EyYzlhZGNmYzE4YzRlM2E3MWFmYWY2MjkiLCJ0eXAiOiJKV1QifQ.eyJ1c2VyX3V1aWQiOiI0NWRkN2NhMC02NmQxLTQxYjgtYTAwZS00NjdiZTNjNzc0NjYiLCJ1c2VyX2lkIjoiNDVkZDdjYTAtNjZkMS00MWI4LWEwMGUtNDY3YmUzYzc3NDY2IiwicGhvbmUiOiI4Mjk5NDQ3MjAwIiwicGFydG5lcklkIjoic2VycHVsY2xpZW50MDAxIiwidXNlclR5cGUiOiJTREtfTUVSQ0hBTlQiLCJzdXBlclBhcnRuZXJJZCI6IlNFUlBVTDAwMSIsImlzcyI6Imh0dHBzOi8vc2VjdXJldG9rZW4uZ29vZ2xlLmNvbS9idWt1d2FydW5nLWFwcCIsImF1ZCI6ImJ1a3V3YXJ1bmctYXBwIiwiYXV0aF90aW1lIjoxNzcxMzk5MDkzLCJzdWIiOiI0NWRkN2NhMC02NmQxLTQxYjgtYTAwZS00NjdiZTNjNzc0NjYiLCJpYXQiOjE3NzEzOTkwOTMsImV4cCI6MTc3MTQwMjY5MywiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6e30sInNpZ25faW5fcHJvdmlkZXIiOiJjdXN0b20ifX0.bNwYYZGGeZ7Gy8s16US6TcIl8of3yXzbX1lVoPB9CooTYa91rS8DnKpPnO5254TYL2WCGSh-Qmbwsc2CZ_Yl51uA_Ti8IFrwT_nn9sL6R-8V3BfxPogP_OmKVi7Gb5_uoUE2Edv2ERozD9Ag2ZzfLPqabiJrTKjDK535Zty0_6mnVw9LuT-iRrbZef7R-OqUbi33HTABNLNrLa8Jg2b1fF3MEhbEJwGqg07QnHEMsqq8YX7fMjE5XP39OfRGm0zdy7syWrZaosRRFmbIZYUZoeTHsI2WOv3ple_r5sRNTlMIMrXo5b2lVQIyjZ0RWfXAflh7VJgdJU1gndpmSJYudA") }
    var resultText by remember { mutableStateOf("") }

    val atmFeatures: AtmFeatures by remember {
        lazy {
            sdk.getAtmFeatures {
                withContext(Dispatchers.Main) {
                    val customToken = accessToken.trim()
                    if (customToken.isNotEmpty()) {
                        resultText += "\n🔑 Using custom access token: ${customToken.take(10)}..."
                        customToken
                    } else {
                        // Token provider function - simulates getting access token from backend
                        // In real implementation, this would call your auth service
                        delay(500) // Simulate network delay
                        "sample_access_token_${System.currentTimeMillis()}"
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        TextField(
            value = accessToken,
            onValueChange = { accessToken = it },
            label = { Text("Access Token") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                resultText += "\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
                resultText += "\n🔍 CHECK BALANCE - Started"
                resultText += "\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

                scope.launch {
                    val result = atmFeatures.checkBalance(
                        accountId = "5252d46d-346a-4e67-a9b7-3ab63a8e4a72",
                        accountType = AccountType.SAVINGS
                    )

                    result.onSuccess { receipt ->
                        resultText += "\n📊 Balance Inquiry Result:"
                        resultText += "\n   Balance: Rp ${receipt.totalAmount}"
                        resultText += "\n   Card: ${receipt.cardNumber}"
                        resultText += "\n   Bank: ${receipt.bankName}"
                        resultText += "\n   Time: ${formatTimestamp(receipt.timestamp.time)}"
                    }.onFailure { error ->
                        resultText += "\n❌ Balance check failed: ${error.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Check Balance")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = resultText,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
