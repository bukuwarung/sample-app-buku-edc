package com.bukuwarung.edc.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bukuwarung.edc.sample.ui.theme.SampleBukuEDCTheme
import com.bukuwarung.edc.ui.HomeScreen
import com.bukuwarung.edc.ui.HomeViewModel
import com.bukuwarung.edc.ui.navigation.Screen
import com.bukuwarung.edc.ui.transfer.*
import com.bukuwarung.edc.ui.theme.Colors
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
                }
            )
        }
        composable(Screen.TransferSelectAccount.route) {
            TransferSelectAccountScreen(
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
                onBack = { navController.popBackStack() },
                onCardDetected = {
                    navController.navigate(Screen.TransferCardInfo.route)
                }
            )
        }
        composable(Screen.TransferCardInfo.route) {
            val viewModel: TransferCardInfoViewModel = hiltViewModel()
            TransferCardInfoScreen(
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
    }
}
