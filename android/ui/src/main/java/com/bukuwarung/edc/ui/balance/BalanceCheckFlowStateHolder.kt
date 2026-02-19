package com.bukuwarung.edc.ui.balance

import com.bukuwarung.edc.domain.transaction.BalanceInfo
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Shared state holder for the balance check flow, accumulating user input across screens.
 *
 * Partners: Similar to [TransferFlowStateHolder], this singleton bridges data between
 * the SelectAccount screen (where account type is chosen) and the BalanceCheckViewModel
 * (where `checkBalance()` is called). Each screen in the Navigation graph creates its
 * own ViewModel via `hiltViewModel()`, so this holder is the mechanism for passing data.
 *
 * Call [clear] when the flow completes or the user navigates away.
 */
@Singleton
class BalanceCheckFlowStateHolder @Inject constructor() {

    /** Account type selected on the Select Account screen ("SAVINGS" or "CHECKING"). */
    var accountType: String = ""

    /**
     * Balance check result from `AtmFeatures.checkBalance()`.
     *
     * Partners: Populated by [BalanceCheckViewModel] after a successful balance check.
     * The summary and receipt screens read from here.
     */
    var balanceResult: BalanceInfo? = null

    /** Resets all state — call when the flow completes or the user cancels. */
    fun clear() {
        accountType = ""
        balanceResult = null
    }
}
