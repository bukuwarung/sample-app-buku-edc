package com.bukuwarung.edc.data.sdk

import android.app.Application
import android.util.Log
import com.bukuwarung.edc.data.BuildConfig
import com.bukuwarung.edc.sdk.BukuEdcSdk
import com.bukuwarung.edc.sdk.factory.BukuEdcSdkFactory
import com.bukuwarung.edc.sdk.logging.SdkLogEvent
import com.bukuwarung.edc.sdk.logging.SdkLogListener
import com.bukuwarung.edc.sdk.model.BukuEdcConfig
import com.bukuwarung.edc.sdk.model.BukuEdcEnv
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wraps [BukuEdcSdk] initialization with proper configuration.
 *
 * Partners: This class demonstrates how to initialize the SDK in your Application class.
 * Replace [BuildConfig.SDK_KEY] with your own SDK key provided by Buku.
 *
 * @see BukuEdcSdk
 * @see BukuEdcConfig
 */
@Singleton
class SdkInitializer @Inject constructor() {

    private lateinit var sdk: BukuEdcSdk

    /**
     * The log listener that forwards SDK logs to Android Logcat.
     * Partners can replace this with their own logging implementation.
     */
    private val logListener = object : SdkLogListener {
        override fun onLogEmitted(logEvent: SdkLogEvent) {
            Log.d(TAG, "[${logEvent.level}] ${logEvent.tag}: ${logEvent.message}")
        }
    }

    /**
     * Initializes the SDK with SANDBOX configuration.
     *
     * Must be called from [Application.onCreate] on the Main Thread before any SDK operations.
     * Uses [BukuEdcSdkFactory.initialize] which returns the SDK instance directly.
     *
     * Partners: Change [BukuEdcEnv.SANDBOX] to [BukuEdcEnv.PRODUCTION] for production builds.
     * Set [BukuEdcConfig.testingMock] to `false` in production.
     */
    fun initialize(application: Application) {
        val config = BukuEdcConfig(
            sdkKey = BuildConfig.SDK_KEY,
            environment = BukuEdcEnv.SANDBOX,
            testingMock = true, // Enable mock responses in SANDBOX for testing without EDC device
            logListener = logListener
        )

        sdk = BukuEdcSdkFactory.initialize(application, config)
        Log.i(TAG, "SDK initialized successfully (SANDBOX, testingMock=true)")
    }

    /**
     * Returns the initialized [BukuEdcSdk] instance.
     * Must be called after [initialize].
     */
    fun getSdk(): BukuEdcSdk = sdk

    companion object {
        private const val TAG = "SdkInitializer"
    }
}
