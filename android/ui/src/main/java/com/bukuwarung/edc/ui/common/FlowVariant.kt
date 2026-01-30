package com.bukuwarung.edc.ui.common

import androidx.annotation.StringRes
import com.bukuwarung.edc.ui.R

enum class FlowVariant {
    Transfer,
    BalanceCheck
}

val FlowVariant.transferSelectAccountScreenTitle: Int
    @StringRes
    get() = if (this == FlowVariant.Transfer) R.string.transfer_title else R.string.balance_check_title

val FlowVariant.transferInsertCardScreenTitle: Int
    @StringRes
    get() = if (this == FlowVariant.Transfer) R.string.transfer_title else R.string.balance_check_title

val FlowVariant.transferCardInfoScreenTitle: Int
    @StringRes
    get() = if (this == FlowVariant.Transfer) R.string.transfer_title else R.string.balance_check_title

val FlowVariant.transferPinScreenTitle: Int
    @StringRes
    get() = if (this == FlowVariant.Transfer) R.string.transfer_masukkan_pin else R.string.transfer_masukkan_pin_atm
