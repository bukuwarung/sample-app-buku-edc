package com.bukuwarung.edc.data.di

import com.bukuwarung.edc.data.sdk.AtmFeaturesFactory
import com.bukuwarung.edc.data.sdk.SdkInitializer
import com.bukuwarung.edc.sdk.AtmFeatures
import com.bukuwarung.edc.sdk.BukuEdcSdk
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Qualifier for the token provider function used by SDK authentication.
 *
 * Partners: The token provider is injected wherever authenticated SDK operations
 * need a fresh auth token. See [SdkModule.provideTokenProvider] for implementation.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenProvider

/**
 * Hilt module providing SDK dependencies.
 *
 * Partners: This module wires up the core SDK objects for dependency injection.
 * - [SdkInitializer] — wraps SDK initialization
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
     *
     * Note: Uses [AtmFeaturesFactory] as a workaround for SDK 0.1.0-SNAPSHOT where
     * `AtmFeatures.create()` was stripped by R8 (missing `@Keep` annotation).
     * Once the SDK team fixes this, replace with: `AtmFeatures.create()`
     */
    @Provides
    @Singleton
    fun provideAtmFeatures(): AtmFeatures {
        return AtmFeaturesFactory.create()
    }

    /**
     * Provides a placeholder token provider for SDK authentication.
     *
     * Partners: Replace this with your actual auth service call, e.g.:
     * ```
     * return { yourAuthService.getAccessToken() }
     * ```
     * The token is used by [BukuEdcSdk.signInUserWithToken] before transactions.
     * The SDK enforces a 3-second timeout on token retrieval.
     */
    @Provides
    @Singleton
    @TokenProvider
    fun provideTokenProvider(): suspend () -> String {
        // Placeholder returning a test token — partners replace with their auth service
        return { "test-partner-token-sandbox" }
    }
}
