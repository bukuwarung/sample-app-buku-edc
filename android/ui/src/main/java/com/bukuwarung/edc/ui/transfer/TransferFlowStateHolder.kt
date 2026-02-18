package com.bukuwarung.edc.ui.transfer

import com.bukuwarung.edc.domain.transaction.TransferReceiptInfo
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Shared state holder for the transfer flow, accumulating user input across screens.
 *
 * Partners: Because each screen in the Navigation graph creates its own ViewModel
 * (via `hiltViewModel()`), transfer parameters entered on earlier screens need to be
 * accessible on later screens (e.g. the Confirm screen needs the bank, amount, etc.
 * entered on the Rekening Tujuan screen). This singleton bridges that gap.
 *
 * Call [clear] when the flow completes or the user navigates away to reset state.
 */
@Singleton
class TransferFlowStateHolder @Inject constructor() {

    /** Account type selected on the Select Account screen ("TABUNGAN" or "GIRO"). */
    var accountType: String = ""

    /** Destination bank name selected on the Pilih Bank screen. */
    var bankName: String = ""

    /** Destination bank code — derived from the bank name for SDK's BankDetails. */
    var bankCode: String = ""

    /** Destination account number entered on the Rekening Tujuan screen. */
    var accountNumber: String = ""

    /** Transfer amount entered on the Rekening Tujuan screen. */
    var amount: String = ""

    /** Optional transfer notes / remarks. */
    var notes: String = ""

    // ——————————————————————————————————————————————————————————————
    // SDK inquiry response — populated after transferInquiry() succeeds
    // ——————————————————————————————————————————————————————————————

    /**
     * Receipt from `transferInquiry()` containing fee breakdown and transaction token.
     *
     * Partners: This is set by [TransferConfirmViewModel] after a successful inquiry call.
     * The [TransferReceiptInfo.transactionToken] is then used for `transferPosting()`.
     */
    var inquiryReceipt: TransferReceiptInfo? = null

    /**
     * Receipt from `transferPosting()` containing final transaction details.
     *
     * Partners: This is set by [TransferConfirmViewModel] after a successful posting call.
     * The success screen reads RRN, approval code, status, and amounts from here.
     */
    var postingReceipt: TransferReceiptInfo? = null

    /** Resets all state — call when the flow completes or the user cancels. */
    fun clear() {
        accountType = ""
        bankName = ""
        bankCode = ""
        accountNumber = ""
        amount = ""
        notes = ""
        inquiryReceipt = null
        postingReceipt = null
    }
}
