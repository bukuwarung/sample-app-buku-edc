package com.bukuwarung.edc.data.sdk;

import android.util.Log;

import com.bukuwarung.edc.sdk.AtmFeatures;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Factory for creating {@link AtmFeatures} instances.
 * <p>
 * The SDK's {@code AtmFeatures.create()} factory method is currently inaccessible
 * because it lacks a {@code @Keep} annotation and was removed by R8 during the SDK build.
 * This factory uses {@code sun.misc.Unsafe.allocateInstance()} as a workaround to create
 * the implementation instance without calling a constructor.
 * <p>
 * <b>Partners</b>: If your SDK version exposes {@code AtmFeatures.create()}, use that instead.
 * This workaround exists only for SDK version 0.1.0-SNAPSHOT.
 */
public final class AtmFeaturesFactory {

    private static final String TAG = "AtmFeaturesFactory";

    private AtmFeaturesFactory() {
        // No instances
    }

    /**
     * Creates an {@link AtmFeatures} instance via reflection.
     *
     * @return a new AtmFeatures instance
     * @throws RuntimeException if creation fails
     */
    public static AtmFeatures create() {
        try {
            Class<?> implClass = Class.forName("com.bukuwarung.edc.sdk.AtmFeaturesImpl");

            // Use Unsafe to allocate instance without constructor
            // (R8 stripped the constructor from SDK 0.1.0-SNAPSHOT)
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field theUnsafe = unsafeClass.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Object unsafe = theUnsafe.get(null);
            Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);

            AtmFeatures instance = (AtmFeatures) allocateInstance.invoke(unsafe, implClass);
            Log.i(TAG, "AtmFeatures instance created via reflection");
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to create AtmFeatures instance. " +
                            "The SDK may need @Keep annotation on AtmFeatures.create(). " +
                            "Contact the SDK team for a fix.", e);
        }
    }
}
