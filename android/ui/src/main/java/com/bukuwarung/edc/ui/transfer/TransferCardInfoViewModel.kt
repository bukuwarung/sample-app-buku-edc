package com.bukuwarung.edc.ui.transfer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransferCardInfoViewModel @Inject constructor() : ViewModel() {
    val cardNumber = "6221 1234 6543 1537"
    val expiryDate = "01/25"
}
