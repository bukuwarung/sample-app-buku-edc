package com.bukuwarung.edc.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bukuwarung.edc.domain.cash.CheckCashWithdrawalEligibilityUseCase
import com.bukuwarung.edc.domain.cash.CheckIsFirstTimeUserUseCase
import com.bukuwarung.edc.domain.settings.AccountSettings
import com.bukuwarung.edc.domain.settings.BankAccount
import com.bukuwarung.edc.domain.settings.GetBankAccountsUseCase
import com.bukuwarung.edc.domain.settings.SettingsRepository
import com.bukuwarung.edc.domain.transaction.CardInfo
import com.bukuwarung.edc.domain.transaction.CardRepository
import com.bukuwarung.edc.domain.transaction.IncompleteTransactionInfo
import com.bukuwarung.edc.ui.theme.Colors
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    onNavigateToTransfer: () -> Unit,
    onNavigateToBalanceCheck: () -> Unit,
    onNavigateToCashWithdrawal: () -> Unit,
    onNavigateToFirstTimeUserPrompt: () -> Unit,
    onNavigateToAddBankAccount: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    // Partners: Observe incomplete transaction state. If a pending transaction is detected
    // on app start, show a dialog prompting the user to acknowledge it.
    val incompleteTransaction by viewModel.incompleteTransaction.collectAsState()
    incompleteTransaction?.let { incomplete ->
        IncompleteTransactionDialog(
            transactionId = incomplete.transactionId,
            type = incomplete.type,
            amount = incomplete.amount.toString(),
            onDismiss = { viewModel.dismissIncompleteTransaction() }
        )
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                HomeUiEvent.NavigateToTransfer -> {
                    onNavigateToTransfer()
                }
                HomeUiEvent.NavigateToBalanceCheck -> {
                    onNavigateToBalanceCheck()
                }
                HomeUiEvent.NavigateToCashWithdrawal -> {
                    onNavigateToCashWithdrawal()
                }
                HomeUiEvent.NavigateToFirstTimeUserPrompt -> {
                    onNavigateToFirstTimeUserPrompt()
                }

                HomeUiEvent.NavigateToAddBankAccount -> {
                    onNavigateToAddBankAccount()
                }
                HomeUiEvent.NavigateToHistory -> {
                    onNavigateToHistory()
                }
                HomeUiEvent.NavigateToSettings -> {
                    onNavigateToSettings()
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Colors.PrimaryGreen)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = stringResource(R.string.home_title),
            color = Colors.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Colors.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.home_mini_atm),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Colors.Black
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bluetooth,
                            contentDescription = stringResource(R.string.content_description_bluetooth),
                            modifier = Modifier.size(24.dp),
                            tint = Colors.Black
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = stringResource(R.string.content_description_print),
                            modifier = Modifier.size(24.dp),
                            tint = Colors.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Grid
                val items = listOf(
                    ActionItemData(HomeAction.Transfer, Icons.Default.CreditCard),
                    ActionItemData(HomeAction.CekSaldo, Icons.Default.CreditCard),
                    ActionItemData(HomeAction.TarikTunai, Icons.Default.FileDownload),
                    ActionItemData(HomeAction.Riwayat, Icons.AutoMirrored.Filled.Assignment),
                    ActionItemData(HomeAction.Pengaturan, Icons.Default.Settings)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.height(280.dp),
                    userScrollEnabled = false
                ) {
                    items(items) { item ->
                        ActionItem(
                            item = item,
                            onClick = { clickedItem ->
                                viewModel.onActionClick(clickedItem.action)
                            }
                        )
                    }
                }
            }
        }
    }
}

data class ActionItemData(
    val action: HomeAction,
    val icon: ImageVector
)

enum class HomeAction(@StringRes val labelResId: Int) {
    Transfer(R.string.home_action_transfer),
    CekSaldo(R.string.home_action_cek_saldo),
    TarikTunai(R.string.home_action_tarik_tunai),
    Riwayat(R.string.home_action_riwayat),
    Pengaturan(R.string.home_action_pengaturan)
}

@Composable
fun ActionItem(
    item: ActionItemData,
    onClick: (ActionItemData) -> Unit
) {
    val label = stringResource(item.action.labelResId)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .size(72.dp)
                .clickable { onClick(item) },
            shape = RoundedCornerShape(16.dp),
            color = Colors.White,
            border = BorderStroke(1.dp, Colors.SurfaceBorder),
            shadowElevation = 1.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = label,
                    modifier = Modifier.size(36.dp),
                    tint = Colors.IconBlue
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = Colors.TextGray
        )
    }
}

/**
 * Dialog shown when an incomplete (pending) transaction is detected on app start.
 *
 * Partners: `AtmFeatures.checkIncompleteTransactions()` is called in [HomeViewModel.init].
 * If a non-null [IncompleteTransactionInfo] is returned, this dialog is displayed with the
 * transaction details. The user can dismiss the dialog to continue using the app.
 *
 * In a production app, you may want to add a "Resume" action that navigates the user
 * to the appropriate transaction flow to complete the pending transaction.
 */
@Composable
private fun IncompleteTransactionDialog(
    transactionId: String,
    type: String,
    amount: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(R.string.incomplete_transaction_title),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(stringResource(R.string.incomplete_transaction_desc))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "${stringResource(R.string.transfer_tipe_transaksi)}: ${formatTransactionType(type)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "ID: $transactionId",
                    fontSize = 12.sp,
                    color = Colors.TextGray
                )
                Text(
                    "${stringResource(R.string.transfer_nominal)}: $amount",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                shape = MaterialTheme.shapes.small
            ) {
                Text(stringResource(R.string.common_tutup), color = Color.White)
            }
        }
    )
}

/**
 * Partners: Maps SDK transaction type strings to user-friendly labels.
 */
@Composable
private fun formatTransactionType(type: String): String = when (type.uppercase()) {
    "TRANSFER" -> stringResource(R.string.transaction_type_transfer)
    "BALANCE" -> stringResource(R.string.transaction_type_balance_inquiry)
    "WITHDRAWAL" -> stringResource(R.string.transaction_type_cash_withdrawal)
    else -> type
}

@Preview(showBackground = true, backgroundColor = Colors.PrimaryGreenColor)
@Composable
fun HomeScreenPreview() {
    val mockRepository = object : SettingsRepository {
        override suspend fun getAccountSettings(): AccountSettings {
            throw NotImplementedError()
        }

        override suspend fun getBankAccounts(): List<BankAccount> {
            return emptyList()
        }

        override fun isFirstTimeUser() = flowOf(false)
        override suspend fun setIsFirstTimeUser(isFirstTime: Boolean) {}
        override fun getPhoneNumber() = flowOf("")
        override suspend fun setPhoneNumber(phoneNumber: String) {}
        override fun getAccessToken() = flowOf("")
        override suspend fun setAccessToken(accessToken: String) {}
        override fun getAccountId() = flowOf("")
        override suspend fun setAccountId(accountId: String) {}
    }

    val mockCardRepository = object : CardRepository {
        override suspend fun getCardInfo(): Result<CardInfo> = Result.success(
            CardInfo(cardNumber = "", cardHolderName = "")
        )
        override suspend fun checkIncompleteTransactions(): Result<IncompleteTransactionInfo?> =
            Result.success(null)
    }

    HomeScreen(
        viewModel = HomeViewModel(
            CheckCashWithdrawalEligibilityUseCase(
                GetBankAccountsUseCase(mockRepository)
            ),
            CheckIsFirstTimeUserUseCase(mockRepository),
            mockCardRepository
        ),
        onNavigateToTransfer = {},
        onNavigateToBalanceCheck = {},
        onNavigateToCashWithdrawal = {},
        onNavigateToFirstTimeUserPrompt = {},
        onNavigateToAddBankAccount = {},
        onNavigateToHistory = {},
        onNavigateToSettings = {}
    )
}
