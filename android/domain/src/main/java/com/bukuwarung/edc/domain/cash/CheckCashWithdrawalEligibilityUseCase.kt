package com.bukuwarung.edc.domain.cash

import com.bukuwarung.edc.domain.settings.GetBankAccountsUseCase
import javax.inject.Inject

class CheckCashWithdrawalEligibilityUseCase @Inject constructor(
    private val getBankAccountsUseCase: GetBankAccountsUseCase
) {
    suspend operator fun invoke(): Boolean {
        return getBankAccountsUseCase().isNotEmpty()
    }
}
