package com.bukuwarung.edc.ui.balance

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BalanceCheckViewModel @Inject constructor() : ViewModel() {
    val balanceAmount: String = "Rp 1.000.000"
    val timestamp: String = "31 Jan 2024 15:08"
    val refNo: String = "739002"
    val accountType: String = "DEBIT TABUNGAN"
    val cardNumber: String = "622112******1537"
    val merchantName: String = "VERIF STORE"
    val merchantAddress: String = "Jl. Pahlawan Revolusi No.13, Pondok Bambu"
    val terminalId: String = "70000001"
    val merchantId: String = "120001202190005"
}
