package com.bukuwarung.edc.sample

import android.app.Application
import android.util.Log
import com.bukuwarung.edc.sdk.BukuEdcSdk
import com.bukuwarung.edc.sdk.factory.BukuEdcSdkFactory
import com.bukuwarung.edc.sdk.logging.SdkLogEvent
import com.bukuwarung.edc.sdk.logging.SdkLogListener
import com.bukuwarung.edc.sdk.model.BukuEdcConfig
import com.bukuwarung.edc.sdk.model.BukuEdcEnv
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BukuEdcApplication : Application(){
    lateinit var sdk: BukuEdcSdk
        private set

    override fun onCreate() {
        super.onCreate()

        // Initialize SDK configuration
        val config = BukuEdcConfig(
            sdkKey = "sample-api-key",
            environment = BukuEdcEnv.SANDBOX,
            testingMock = true, // Enable mock by default for sandbox to match working curl
            logListener = object : SdkLogListener {
                override fun onLogEmitted(logEvent: SdkLogEvent) {
                    Log.d(logEvent.tag, logEvent.message)
                }
            }
        )

        // Initialize SDK
        // This handles binding and background initialization automatically
        sdk = BukuEdcSdkFactory.initialize(this, config)
    }
}
