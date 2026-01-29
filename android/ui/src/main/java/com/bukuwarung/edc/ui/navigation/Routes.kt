package com.bukuwarung.edc.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object TransferSelectAccount : Screen("transfer_select_account")
    data object TransferPilihBank : Screen("transfer_pilih_bank")
    data object TransferRekeningTujuan : Screen("transfer_rekening_tujuan")
    data object TransferInsertCard : Screen("transfer_insert_card")
    data object TransferCardInfo : Screen("transfer_card_info")
    data object TransferPin : Screen("transfer_pin")
    data object TransferConfirm : Screen("transfer_confirm")
    data object TransferSuccess : Screen("transfer_success")
}
