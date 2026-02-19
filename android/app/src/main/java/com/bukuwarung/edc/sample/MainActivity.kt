package com.bukuwarung.edc.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bukuwarung.edc.sample.ui.theme.SampleBukuEDCTheme
import com.bukuwarung.edc.ui.HomeScreen
import com.bukuwarung.edc.ui.HomeViewModel
import com.bukuwarung.edc.ui.activation.ActivationScreen
import com.bukuwarung.edc.ui.balance.BalanceCheckFlowStateHolder
import com.bukuwarung.edc.ui.balance.BalanceCheckReceiptScreen
import com.bukuwarung.edc.ui.balance.BalanceCheckSummaryScreen
import com.bukuwarung.edc.ui.balance.BalanceCheckViewModel
import com.bukuwarung.edc.ui.cash.CashWithdrawalFirstTimeUserScreen
import com.bukuwarung.edc.ui.common.FlowVariant
import com.bukuwarung.edc.ui.developer.DeveloperSettingsScreen
import com.bukuwarung.edc.ui.developer.DeveloperSettingsViewModel
import com.bukuwarung.edc.ui.history.HistoryScreen
import com.bukuwarung.edc.ui.history.HistoryViewModel
import com.bukuwarung.edc.ui.navigation.Screen
import com.bukuwarung.edc.ui.settings.SettingsAccountScreen
import com.bukuwarung.edc.ui.settings.SettingsBankAccountsScreen
import com.bukuwarung.edc.ui.settings.SettingsEditStoreNameScreen
import com.bukuwarung.edc.ui.settings.SettingsMenuScreen
import com.bukuwarung.edc.ui.theme.Colors
import com.bukuwarung.edc.ui.transfer.TransferCardInfoScreen
import com.bukuwarung.edc.ui.transfer.TransferCardInfoViewModel
import com.bukuwarung.edc.ui.transfer.TransferConfirmScreen
import com.bukuwarung.edc.ui.transfer.TransferConfirmViewModel
import com.bukuwarung.edc.ui.transfer.TransferInsertCardScreen
import com.bukuwarung.edc.ui.transfer.TransferInsertCardViewModel
import com.bukuwarung.edc.ui.transfer.TransferPilihBankScreen
import com.bukuwarung.edc.ui.transfer.TransferPilihBankViewModel
import com.bukuwarung.edc.ui.transfer.TransferPinScreen
import com.bukuwarung.edc.ui.transfer.TransferPinViewModel
import com.bukuwarung.edc.ui.transfer.TransferRekeningTujuanScreen
import com.bukuwarung.edc.ui.transfer.TransferRekeningTujuanViewModel
import com.bukuwarung.edc.ui.transfer.TransferSelectAccountScreen
import com.bukuwarung.edc.ui.transfer.TransferFlowStateHolder
import com.bukuwarung.edc.ui.transfer.TransferSuccessScreen
import com.bukuwarung.edc.ui.transfer.TransferSuccessViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Partners: TransferFlowStateHolder is a @Singleton that accumulates user input
     * across the multi-screen transfer flow. It is populated here in the navigation
     * callbacks and consumed by ViewModels via Hilt injection.
     */
    @Inject lateinit var transferFlowState: TransferFlowStateHolder

    /**
     * Partners: BalanceCheckFlowStateHolder is a @Singleton that accumulates user input
     * across the balance check flow. The selected account type is saved here in the
     * navigation callback and consumed by BalanceCheckViewModel via Hilt injection.
     */
    @Inject lateinit var balanceCheckFlowState: BalanceCheckFlowStateHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleBukuEDCTheme(darkTheme = false) {
                Surface(color = Colors.White) {
                    MainNavigation(
                        intent = intent,
                        transferFlowState = transferFlowState,
                        balanceCheckFlowState = balanceCheckFlowState
                    )
                }
            }
        }
    }
}

@Composable
fun MainNavigation(
    intent: Intent?,
    transferFlowState: TransferFlowStateHolder,
    balanceCheckFlowState: BalanceCheckFlowStateHolder
) {
    val navController = rememberNavController()

    LaunchedEffect(intent) {
        if (intent?.action == "com.bukuwarung.edc.ACTION_DEVELOPER_SETTINGS") {
            navController.navigate(Screen.DeveloperSettings.route)
        }
    }

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel,
                onNavigateToTransfer = {
                    navController.navigate(Screen.TransferSelectAccount.route)
                },
                onNavigateToBalanceCheck = {
                    navController.navigate(Screen.BalanceCheckSelectAccount.route)
                },
                onNavigateToCashWithdrawal = {
                    navController.navigate(Screen.CashWithdrawalSelectAccount.route)
                },
                onNavigateToFirstTimeUserPrompt = {
                    navController.navigate(Screen.Activation.route)
                },
                onNavigateToAddBankAccount = {
                    navController.navigate(Screen.SettingsBankAccounts.route)
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        composable(Screen.TransferSelectAccount.route) {
            // Partners: Clear any previous transfer state when starting a new flow.
            TransferSelectAccountScreen(
                variant = FlowVariant.Transfer,
                onBack = { navController.popBackStack() },
                onAccountSelected = { accountType ->
                    // Partners: Save the selected account type (TABUNGAN → SAVINGS, GIRO → CHECKING)
                    // for the transferInquiry() call later.
                    transferFlowState.clear()
                    transferFlowState.accountType = if (accountType == "GIRO") "CHECKING" else "SAVINGS"
                    navController.navigate(Screen.TransferPilihBank.route)
                }
            )
        }
        composable(Screen.TransferPilihBank.route) {
            val viewModel: TransferPilihBankViewModel = hiltViewModel()
            TransferPilihBankScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onBankSelected = { bank ->
                    // Partners: Save the selected bank name and derive a bank code.
                    // In production, use actual bank codes from your bank list API.
                    transferFlowState.bankName = bank
                    transferFlowState.bankCode = bank.uppercase()
                    navController.navigate(Screen.TransferRekeningTujuan.route)
                }
            )
        }
        composable(Screen.TransferRekeningTujuan.route) {
            val viewModel: TransferRekeningTujuanViewModel = hiltViewModel()
            TransferRekeningTujuanScreen(
                viewModel = viewModel,
                bankName = transferFlowState.bankName,
                onBack = { navController.popBackStack() },
                onContinue = { account, amount, remark ->
                    // Partners: Save the destination account, amount, and notes
                    // for the transferInquiry() call on the Confirm screen.
                    transferFlowState.accountNumber = account
                    transferFlowState.amount = amount
                    transferFlowState.notes = remark
                    navController.navigate(Screen.TransferInsertCard.route)
                }
            )
        }
        composable(Screen.TransferInsertCard.route) {
            val viewModel: TransferInsertCardViewModel = hiltViewModel()
            TransferInsertCardScreen(
                variant = FlowVariant.Transfer,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCardDetected = {
                    navController.navigate(Screen.TransferCardInfo.route)
                }
            )
        }
        composable(Screen.TransferCardInfo.route) {
            val viewModel: TransferCardInfoViewModel = hiltViewModel()
            TransferCardInfoScreen(
                variant = FlowVariant.Transfer,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onContinue = {
                    navController.navigate(Screen.TransferPin.route)
                }
            )
        }
        composable(Screen.TransferPin.route) {
            val viewModel: TransferPinViewModel = hiltViewModel()
            TransferPinScreen(
                variant = FlowVariant.Transfer,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onPinEntered = { pin ->
                    navController.navigate(Screen.TransferConfirm.route)
                }
            )
        }
        composable(Screen.TransferConfirm.route) {
            val viewModel: TransferConfirmViewModel = hiltViewModel()
            TransferConfirmScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onConfirm = {
                    navController.navigate(Screen.TransferSuccess.route)
                },
                onCancel = {
                    transferFlowState.clear()
                    navController.popBackStack(Screen.Home.route, false)
                }
            )
        }
        composable(Screen.TransferSuccess.route) {
            val viewModel: TransferSuccessViewModel = hiltViewModel()
            TransferSuccessScreen(
                viewModel = viewModel,
                onClose = {
                    transferFlowState.clear()
                    navController.popBackStack(Screen.Home.route, false)
                }
            )
        }

        // Balance Check Flow
        composable(Screen.BalanceCheckSelectAccount.route) {
            // Partners: Clear any previous balance check state when starting a new flow.
            TransferSelectAccountScreen(
                variant = FlowVariant.BalanceCheck,
                onBack = { navController.popBackStack() },
                onAccountSelected = { accountType ->
                    // Partners: Save the selected account type (TABUNGAN → SAVINGS, GIRO → CHECKING)
                    // for the checkBalance() call later.
                    balanceCheckFlowState.clear()
                    balanceCheckFlowState.accountType = if (accountType == "GIRO") "CHECKING" else "SAVINGS"
                    navController.navigate(Screen.BalanceCheckInsertCard.route)
                }
            )
        }
        composable(Screen.BalanceCheckInsertCard.route) {
            val viewModel: TransferInsertCardViewModel = hiltViewModel()
            TransferInsertCardScreen(
                variant = FlowVariant.BalanceCheck,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCardDetected = {
                    navController.navigate(Screen.BalanceCheckCardInfo.route)
                }
            )
        }
        composable(Screen.BalanceCheckCardInfo.route) {
            val viewModel: TransferCardInfoViewModel = hiltViewModel()
            TransferCardInfoScreen(
                variant = FlowVariant.BalanceCheck,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onContinue = {
                    navController.navigate(Screen.BalanceCheckPin.route)
                }
            )
        }
        composable(Screen.BalanceCheckPin.route) {
            val viewModel: TransferPinViewModel = hiltViewModel()
            TransferPinScreen(
                variant = FlowVariant.BalanceCheck,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onPinEntered = { pin ->
                    navController.navigate(Screen.BalanceCheckSummary.route)
                }
            )
        }
        composable(Screen.BalanceCheckSummary.route) {
            val viewModel: BalanceCheckViewModel = hiltViewModel()
            BalanceCheckSummaryScreen(
                viewModel = viewModel,
                onClose = {
                    navController.popBackStack(Screen.Home.route, false)
                },
                onShowReceipt = {
                    navController.navigate(Screen.BalanceCheckReceipt.route)
                }
            )
        }
        composable(Screen.BalanceCheckReceipt.route) {
            val viewModel: BalanceCheckViewModel = hiltViewModel()
            BalanceCheckReceiptScreen(
                viewModel = viewModel,
                onClose = {
                    navController.popBackStack(Screen.Home.route, false)
                }
            )
        }

        // Settings Flow
        composable(Screen.Settings.route) {
            SettingsMenuScreen(
                onBack = { navController.popBackStack() },
                onAccountClick = { navController.navigate(Screen.SettingsAccount.route) },
                onBankAccountsClick = { navController.navigate(Screen.SettingsBankAccounts.route) }
            )
        }
        composable(Screen.SettingsAccount.route) {
            SettingsAccountScreen(
                onBack = { navController.popBackStack() },
                onEditStoreNameClick = { navController.navigate(Screen.SettingsEditStoreName.route) }
            )
        }
        composable(Screen.SettingsEditStoreName.route) {
            SettingsEditStoreNameScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.SettingsBankAccounts.route) {
            SettingsBankAccountsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Cash Withdrawal Flow
        composable(Screen.Activation.route) {
            ActivationScreen(
                onBack = { navController.popBackStack() },
                onContinue = { phoneNumber ->
                    navController.popBackStack()
                    navController.navigate(Screen.CashWithdrawalFirstTimeUser.route)
                }
            )
        }
        composable(Screen.CashWithdrawalFirstTimeUser.route) {
            CashWithdrawalFirstTimeUserScreen(
                onAddAccount = {
                    navController.navigate(Screen.SettingsBankAccounts.route)
                },
                onLater = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.CashWithdrawalSelectAccount.route) {
            // Partners: Cash withdrawal reuses the same TransferFlowStateHolder and
            // TransferRepository as the transfer flow. The key difference is setting
            // isCashWithdrawal = true, which is passed to AtmFeatures.transferInquiry().
            TransferSelectAccountScreen(
                variant = FlowVariant.CashWithdrawal,
                onBack = { navController.popBackStack() },
                onAccountSelected = { accountType ->
                    // Partners: Clear previous state and mark this as a cash withdrawal flow.
                    // The SDK reuses the transfer API with isCashWithdrawal = true.
                    transferFlowState.clear()
                    transferFlowState.isCashWithdrawal = true
                    transferFlowState.accountType = if (accountType == "GIRO") "CHECKING" else "SAVINGS"
                    navController.navigate(Screen.CashWithdrawalInsertCard.route)
                }
            )
        }
        composable(Screen.CashWithdrawalInsertCard.route) {
            val viewModel: TransferInsertCardViewModel = hiltViewModel()
            TransferInsertCardScreen(
                variant = FlowVariant.CashWithdrawal,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCardDetected = {
                    navController.navigate(Screen.CashWithdrawalCardInfo.route)
                }
            )
        }
        composable(Screen.CashWithdrawalCardInfo.route) {
            val viewModel: TransferCardInfoViewModel = hiltViewModel()
            TransferCardInfoScreen(
                variant = FlowVariant.CashWithdrawal,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onContinue = {
                    navController.navigate(Screen.CashWithdrawalPin.route)
                }
            )
        }
        composable(Screen.CashWithdrawalPin.route) {
            val viewModel: TransferPinViewModel = hiltViewModel()
            TransferPinScreen(
                variant = FlowVariant.CashWithdrawal,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onPinEntered = { pin ->
                    navController.navigate(Screen.CashWithdrawalConfirm.route)
                }
            )
        }
        composable(Screen.CashWithdrawalConfirm.route) {
            val viewModel: TransferConfirmViewModel = hiltViewModel()
            TransferConfirmScreen(
                variant = FlowVariant.CashWithdrawal,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onConfirm = {
                    navController.navigate(Screen.CashWithdrawalSuccess.route)
                },
                onCancel = {
                    transferFlowState.clear()
                    navController.popBackStack(Screen.Home.route, false)
                }
            )
        }
        composable(Screen.CashWithdrawalSuccess.route) {
            val viewModel: TransferSuccessViewModel = hiltViewModel()
            TransferSuccessScreen(
                viewModel = viewModel,
                onClose = {
                    transferFlowState.clear()
                    navController.popBackStack(Screen.Home.route, false)
                }
            )
        }
        // Transaction History
        // Partners: The History screen retrieves paginated transaction records via
        // AtmFeatures.getTransactionHistory(). HistoryViewModel handles pagination
        // automatically — the screen supports infinite-scroll loading.
        composable(Screen.History.route) {
            val viewModel: HistoryViewModel = hiltViewModel()
            HistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.DeveloperSettings.route) {
            val viewModel: DeveloperSettingsViewModel = hiltViewModel()
            DeveloperSettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
