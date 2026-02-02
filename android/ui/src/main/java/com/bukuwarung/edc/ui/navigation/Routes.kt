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

    // Balance Check Flow
    data object BalanceCheckSelectAccount : Screen("balance_check_select_account")
    data object BalanceCheckInsertCard : Screen("balance_check_insert_card")
    data object BalanceCheckCardInfo : Screen("balance_check_card_info")
    data object BalanceCheckPin : Screen("balance_check_pin")
    data object BalanceCheckSummary : Screen("balance_check_summary")
    data object BalanceCheckReceipt : Screen("balance_check_receipt")

    // Settings Flow
    data object Settings : Screen("settings")
    data object SettingsAccount : Screen("settings_account")
    data object SettingsEditStoreName : Screen("settings_edit_store_name")
    data object SettingsBankAccounts : Screen("settings_bank_accounts")

    // Cash Withdrawal Flow
    data object CashWithdrawalFirstTimeUser : Screen("cash_withdrawal_first_time_user")
    data object CashWithdrawalSelectAccount : Screen("cash_withdrawal_select_account")
    data object CashWithdrawalInsertCard : Screen("cash_withdrawal_insert_card")
    data object CashWithdrawalCardInfo : Screen("cash_withdrawal_card_info")
    data object CashWithdrawalPin : Screen("cash_withdrawal_pin")
    data object CashWithdrawalConfirm : Screen("cash_withdrawal_confirm")
    data object CashWithdrawalSuccess : Screen("cash_withdrawal_success")
    data object Activation : Screen("activation")
}
