package com.bukuwarung.edc.ui.transfer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TransferPilihBankViewModel @Inject constructor() : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val allBanks = listOf(
        "BANK Daerah", "BCA", "BNI", "BRI", "BSI", "BSS", "CIMB", "Mandiri"
    )

    private val _banks = MutableStateFlow(allBanks)
    val banks: StateFlow<List<String>> = _banks.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _banks.value = if (query.isEmpty()) {
            allBanks
        } else {
            allBanks.filter { it.contains(query, ignoreCase = true) }
        }
    }
}
