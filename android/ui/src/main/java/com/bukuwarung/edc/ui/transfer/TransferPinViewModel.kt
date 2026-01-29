package com.bukuwarung.edc.ui.transfer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TransferPinViewModel @Inject constructor() : ViewModel() {
    private val _pin = MutableStateFlow("")
    val pin: StateFlow<String> = _pin.asStateFlow()

    fun onPinChange(newPin: String) {
        if (newPin.length <= 6) {
            _pin.value = newPin
        }
    }
}
