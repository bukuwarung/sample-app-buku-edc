package com.bukuwarung.edc.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
 * Reusable error state composable for displaying SDK error messages across all flows.
 *
 * Partners: The SDK throws four exception types, each requiring different user guidance:
 *
 * **DeviceSdkException** — EDC device/hardware errors. Common codes:
 * - `E01` — Card read error (card not inserted properly or unreadable)
 * - `E02` — Card removed prematurely during transaction
 * - `E06` — PIN entry cancelled by the cardholder
 * - `E21` — Transaction timeout (device or backend did not respond in time)
 * - `E99` — Unknown device error
 *
 * **BackendException** — Backend processing errors. Common codes:
 * - `30` — Format error (malformed request data)
 * - `55` — Invalid PIN entered by cardholder
 * - `03` — Invalid merchant (merchant not registered or blocked)
 *
 * **TokenExpiredException** — The single-use transaction token from `transferInquiry()`
 * has exceeded its 15-minute validity window. The user must restart the transfer/withdrawal flow.
 *
 * **InvalidTokenException** — The transaction token is invalid (already used or malformed).
 * The user must restart the transfer/withdrawal flow.
 *
 * Usage:
 * ```kotlin
 * ErrorState(
 *     title = "Transaksi Gagal",
 *     message = getErrorMessage(error),         // from ViewModel
 *     onRetry = { viewModel.retry() },          // null if retry not applicable
 *     onBack = { navController.popBackStack() } // null if back not applicable
 * )
 * ```
 *
 * @param title Error title displayed prominently (e.g. "Transaksi Gagal")
 * @param message Detailed error message from the ViewModel's `getErrorMessage()` mapper
 * @param onRetry Optional retry callback — shown as primary button. Pass `null` to hide.
 *   Typically used for DeviceSdkException and BackendException (transient errors).
 * @param onBack Optional back/navigate callback — shown as text button. Pass `null` to hide.
 *   Typically used for TokenExpiredException/InvalidTokenException (must restart flow).
 * @param modifier Optional modifier for the root container
 */
@Composable
fun ErrorState(
    title: String,
    message: String,
    onRetry: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // Partners: Display the error title prominently.
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Partners: Display the detailed error message.
            // This typically includes the SDK error code and description
            // (e.g. "Kesalahan perangkat (E01): Card read error").
            Text(
                text = message,
                color = Colors.TextGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Partners: Show retry button for transient errors (DeviceSdkException,
            // BackendException). These errors may succeed on a second attempt.
            if (onRetry != null) {
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(stringResource(R.string.common_retry), color = Color.White)
                }
            }

            // Partners: Show back button for non-recoverable errors (TokenExpiredException,
            // InvalidTokenException). The user must navigate back and restart the flow.
            if (onBack != null) {
                if (onRetry != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                TextButton(onClick = onBack) {
                    Text(
                        stringResource(R.string.common_back),
                        color = Color(0xFF1565C0)
                    )
                }
            }
        }
    }
}
