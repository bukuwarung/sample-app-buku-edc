package com.bukuwarung.edc.data.di

import com.bukuwarung.edc.data.card.CardRepositoryImpl
import com.bukuwarung.edc.data.sdk.AuthTokenProvider
import com.bukuwarung.edc.data.sdk.SdkInitializer
import com.bukuwarung.edc.data.transaction.TransactionEventRepositoryImpl
import com.bukuwarung.edc.data.transaction.TransferRepositoryImpl
import com.bukuwarung.edc.domain.transaction.CardRepository
import com.bukuwarung.edc.domain.transaction.TransactionEventRepository
import com.bukuwarung.edc.domain.transaction.TransferRepository
import com.bukuwarung.edc.sdk.AtmFeatures
import com.bukuwarung.edc.sdk.BukuEdcSdk
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing SDK dependencies.
 *
 * Partners: This module wires up the core SDK objects for dependency injection.
 * - [SdkInitializer] — wraps SDK initialization via [BukuEdcSdkFactory]
 * - [BukuEdcSdk] — main SDK interface for device-level operations
 * - [AtmFeatures] — transaction operations (balance, transfer, withdrawal)
 * - Token provider — your auth token retrieval function
 */
@Module
@InstallIn(SingletonComponent::class)
object SdkModule {

    /**
     * Provides the [BukuEdcSdk] instance obtained from [SdkInitializer].
     * The SDK must be initialized before this is used (see BukuEdcApplication in the app module).
     */
    @Provides
    @Singleton
    fun provideBukuEdcSdk(sdkInitializer: SdkInitializer): BukuEdcSdk {
        return sdkInitializer.getSdk()
    }

    /**
     * Provides the [AtmFeatures] instance for performing ATM transactions.
     *
     * Partners: AtmFeatures is the main interface for all transaction operations
     * (balance check, transfer inquiry/posting, card info).
     * The [AuthTokenProvider] is called before each transaction to supply your auth token.
     */
    @Provides
    @Singleton
    fun provideAtmFeatures(
        sdk: BukuEdcSdk,
        tokenProvider: AuthTokenProvider
    ): AtmFeatures {
        return sdk.getAtmFeatures { tokenProvider.getToken() }
    }

    /**
     * Provides the [CardRepository] backed by [CardRepositoryImpl].
     *
     * Partners: [CardRepository] handles card-read and incomplete transaction operations.
     * Swap the binding here if you want to inject a test double during development.
     */
    @Provides
    @Singleton
    fun provideCardRepository(impl: CardRepositoryImpl): CardRepository = impl

    /**
     * Provides the [TransactionEventRepository] backed by [TransactionEventRepositoryImpl].
     *
     * Partners: Inject [TransactionEventRepository] in ViewModels to observe real-time
     * transaction events (WaitingForCard → CardDetected → EnteringPin → ProcessingTransaction
     * → TransactionComplete/Failed) during active SDK operations.
     */
    @Provides
    @Singleton
    fun provideTransactionEventRepository(impl: TransactionEventRepositoryImpl): TransactionEventRepository = impl

    /**
     * Provides the [TransferRepository] backed by [TransferRepositoryImpl].
     *
     * Partners: [TransferRepository] handles the two-step transfer flow:
     * 1. `transferInquiry()` — retrieves fees and a single-use transaction token
     * 2. `transferPosting()` — executes the transfer using the token (valid 15 min)
     *
     * The same repository is used for cash withdrawals (with `isCashWithdrawal = true`).
     */
    @Provides
    @Singleton
    fun provideTransferRepository(impl: TransferRepositoryImpl): TransferRepository = impl

    /**
     * Provides the [AuthTokenProvider] for SDK authentication.
     *
     * Partners: Replace the lambda body with your actual auth service call, e.g.:
     * ```
     * AuthTokenProvider { yourAuthService.getAccessToken() }
     * ```
     * The token is used by [AtmFeatures] before transactions.
     * The SDK enforces a 3-second timeout on token retrieval.
     */
    @Provides
    @Singleton
    fun provideAuthTokenProvider(): AuthTokenProvider {
        // Placeholder returning a test token — partners replace with their auth service
        return AuthTokenProvider { "test-partner-token-sandbox" }
    }
}
