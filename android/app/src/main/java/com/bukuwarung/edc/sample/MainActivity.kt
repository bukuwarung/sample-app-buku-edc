package com.bukuwarung.edc.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bukuwarung.edc.sample.ui.theme.SampleBukuEDCTheme
import com.bukuwarung.edc.ui.HomeScreen
import com.bukuwarung.edc.ui.HomeViewModel
import com.bukuwarung.edc.ui.balance.BalanceCheckReceiptScreen
import com.bukuwarung.edc.ui.balance.BalanceCheckSummaryScreen
import com.bukuwarung.edc.ui.balance.BalanceCheckViewModel
import com.bukuwarung.edc.ui.common.FlowVariant
import com.bukuwarung.edc.ui.navigation.Screen
import com.bukuwarung.edc.ui.settings.SettingsAccountScreen
import com.bukuwarung.edc.ui.settings.SettingsBankAccountsScreen
import com.bukuwarung.edc.ui.settings.SettingsEditStoreNameScreen
import com.bukuwarung.edc.ui.settings.SettingsMenuScreen
import com.bukuwarung.edc.ui.theme.Colors
import com.bukuwarung.edc.ui.transfer.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleBukuEDCTheme(darkTheme = false) {
                Surface(color = Colors.White) {
                    MainNavigation()
                }
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
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
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        composable(Screen.TransferSelectAccount.route) {
            TransferSelectAccountScreen(
                variant = FlowVariant.Transfer,
                onBack = { navController.popBackStack() },
                onAccountSelected = {
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
                    navController.navigate(Screen.TransferRekeningTujuan.route)
                }
            )
        }
        composable(Screen.TransferRekeningTujuan.route) {
            val viewModel: TransferRekeningTujuanViewModel = hiltViewModel()
            TransferRekeningTujuanScreen(
                viewModel = viewModel,
                bankName = "Mandiri", // Mocked for simplicity
                onBack = { navController.popBackStack() },
                onContinue = { account, amount, remark ->
                    navController.navigate(Screen.TransferInsertCard.route)
                }
            )
        }
        composable(Screen.TransferInsertCard.route) {
            TransferInsertCardScreen(
                variant = FlowVariant.Transfer,
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
                    navController.popBackStack(Screen.Home.route, false)
                }
            )
        }
        composable(Screen.TransferSuccess.route) {
            val viewModel: TransferSuccessViewModel = hiltViewModel()
            TransferSuccessScreen(
                viewModel = viewModel,
                onClose = {
                    navController.popBackStack(Screen.Home.route, false)
                }
            )
        }

        // Balance Check Flow
        composable(Screen.BalanceCheckSelectAccount.route) {
            TransferSelectAccountScreen(
                variant = FlowVariant.BalanceCheck,
                onBack = { navController.popBackStack() },
                onAccountSelected = {
                    navController.navigate(Screen.BalanceCheckInsertCard.route)
                }
            )
        }
        composable(Screen.BalanceCheckInsertCard.route) {
            TransferInsertCardScreen(
                variant = FlowVariant.BalanceCheck,
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
    }
}
