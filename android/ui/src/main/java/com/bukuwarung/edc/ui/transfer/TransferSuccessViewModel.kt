package com.bukuwarung.edc.ui.transfer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransferSuccessViewModel @Inject constructor() : ViewModel() {
    val amount = "Rp1.000.000"
    val date = "31 Jan 2024 15:08"
    val refNo = "739002"
}
