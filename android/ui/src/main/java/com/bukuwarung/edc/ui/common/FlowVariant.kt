package com.bukuwarung.edc.ui.common

import androidx.annotation.StringRes
import com.bukuwarung.edc.ui.R

enum class FlowVariant {
    Transfer,
    BalanceCheck,
    CashWithdrawal
}

val FlowVariant.transferSelectAccountScreenTitle: Int
    @StringRes
    get() = when (this) {
        FlowVariant.Transfer -> R.string.transfer_title
        FlowVariant.BalanceCheck -> R.string.balance_check_title
        FlowVariant.CashWithdrawal -> R.string.tarik_tunai_title
    }

val FlowVariant.transferInsertCardScreenTitle: Int
    @StringRes
    get() = when (this) {
        FlowVariant.Transfer -> R.string.transfer_title
        FlowVariant.BalanceCheck -> R.string.balance_check_title
        FlowVariant.CashWithdrawal -> R.string.tarik_tunai_title
    }

val FlowVariant.transferCardInfoScreenTitle: Int
    @StringRes
    get() = when (this) {
        FlowVariant.Transfer -> R.string.transfer_title
        FlowVariant.BalanceCheck -> R.string.balance_check_title
        FlowVariant.CashWithdrawal -> R.string.tarik_tunai_title
    }

val FlowVariant.transferPinScreenTitle: Int
    @StringRes
    get() = when (this) {
        FlowVariant.Transfer -> R.string.transfer_masukkan_pin
        FlowVariant.BalanceCheck -> R.string.transfer_masukkan_pin_atm
        FlowVariant.CashWithdrawal -> R.string.transfer_masukkan_pin_atm
    }
