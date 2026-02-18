package com.bukuwarung.edc.ui.transfer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigInteger
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel for the Transfer Success screen.
 *
 * Partners: This ViewModel reads the posting receipt from [TransferFlowStateHolder],
 * which was populated by [TransferConfirmViewModel] after a successful
 * `AtmFeatures.transferPosting()` call. The receipt contains the final transaction
 * details: amount, admin fee, total, RRN, approval code, and status.
 *
 * The success screen displays these fields as a transaction receipt for the customer.
 */
@HiltViewModel
class TransferSuccessViewModel @Inject constructor(
    private val flowState: TransferFlowStateHolder
) : ViewModel() {

    private val receipt = flowState.postingReceipt

    /** Formatted total transfer amount (e.g. "Rp1.000.000"). */
    val totalAmount: String = receipt?.let { formatRupiah(it.totalAmount) } ?: "-"

    /** Transfer amount before fees. */
    val amount: String = receipt?.let { formatRupiah(it.amount) } ?: "-"

    /** Admin fee charged for the transfer. */
    val adminFee: String = receipt?.let { formatRupiah(it.adminFee) } ?: "-"

    /** Formatted transaction timestamp (e.g. "31 Jan 2024 15:08"). */
    val date: String = receipt?.let {
        SimpleDateFormat("dd MMM yyyy HH:mm", Locale("id", "ID")).format(it.timestamp)
    } ?: "-"

    /**
     * Retrieval Reference Number — unique identifier from the bank.
     *
     * Partners: The RRN is the primary reference for dispute resolution.
     * Always display this on the receipt and advise customers to keep it.
     */
    val rrn: String = receipt?.rrn ?: "-"

    /**
     * Approval code from the acquiring bank.
     *
     * Partners: This confirms the transaction was authorized by the bank.
     */
    val approvalCode: String = receipt?.approvalCode ?: "-"

    /** Transaction status (e.g. "SUCCESS"). */
    val status: String = receipt?.status ?: "-"

    /** Clears the flow state when the success screen is closed. */
    fun clearFlowState() {
        flowState.clear()
    }

    private fun formatRupiah(value: BigInteger): String {
        val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
        return "Rp${formatter.format(value)}"
    }
}
