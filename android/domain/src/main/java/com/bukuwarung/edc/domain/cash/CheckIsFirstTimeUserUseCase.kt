package com.bukuwarung.edc.domain.cash

import javax.inject.Inject

class CheckIsFirstTimeUserUseCase @Inject constructor() {
    suspend operator fun invoke(): Boolean {
        return true
    }
}
