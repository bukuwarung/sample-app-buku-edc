package com.bukuwarung.edc.ui.transfer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TransferRekeningTujuanViewModel @Inject constructor() : ViewModel() {
    private val _bankName = MutableStateFlow("")
    val bankName: StateFlow<String> = _bankName.asStateFlow()

    private val _accountNumber = MutableStateFlow("")
    val accountNumber: StateFlow<String> = _accountNumber.asStateFlow()

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _remark = MutableStateFlow("")
    val remark: StateFlow<String> = _remark.asStateFlow()

    fun setBankName(name: String) {
        _bankName.value = name
    }

    fun onAccountNumberChange(value: String) {
        _accountNumber.value = value
    }

    fun onAmountChange(value: String) {
        _amount.value = value
    }

    fun onRemarkChange(value: String) {
        _remark.value = value
    }
}
