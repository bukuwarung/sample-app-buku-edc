package com.bukuwarung.edc.data.sdk

/**
 * Functional interface for retrieving an authentication token for SDK operations.
 *
 * Partners: Implement this interface with your real auth service call, e.g.:
 * ```
 * AuthTokenProvider { yourAuthService.getAccessToken() }
 * ```
 * The token is passed to [com.bukuwarung.edc.sdk.AtmFeatures] via `sdk.getAtmFeatures { token }`
 * before each ATM transaction. The SDK enforces a 3-second timeout on token retrieval.
 */
fun interface AuthTokenProvider {
    /** Returns a fresh authentication token. Must complete within 3 seconds. */
    suspend fun getToken(): String
}
