package com.bukuwarung.edc.ui.transfer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransferConfirmViewModel @Inject constructor() : ViewModel() {
    val type = "Transfer"
    val bankName = "BCA"
    val accountNo = "8372131455"
    val accountName = "Pramudiana"
    val amount = "Rp1.000.000"
    val remark = "Pembayaran Hutang"
}
