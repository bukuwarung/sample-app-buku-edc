package com.bukuwarung.edc.sample

import android.app.Application
import com.bukuwarung.edc.data.sdk.SdkInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application class that initializes the Buku EDC SDK on startup.
 *
 * Partners: SDK initialization must happen on the Main Thread in [onCreate],
 * before any transaction operations are performed.
 */
@HiltAndroidApp
class BukuEdcApplication : Application() {

    @Inject
    lateinit var sdkInitializer: SdkInitializer

    override fun onCreate() {
        super.onCreate() // Hilt injection happens here
        // Initialize SDK on Main Thread — required by the SDK contract
        sdkInitializer.initialize(this)
    }
}
