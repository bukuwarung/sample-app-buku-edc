package com.bukuwarung.edc.data.util

import kotlin.coroutines.cancellation.CancellationException

/**
 * A coroutine-safe version of [runCatching] that rethrows [CancellationException].
 *
 * Partners: The standard [runCatching] catches ALL exceptions including [CancellationException],
 * which breaks structured concurrency — a cancelled coroutine would silently return
 * `Result.failure(CancellationException)` instead of propagating the cancellation.
 *
 * Use this in all `suspend` functions that wrap SDK calls with `Result<T>`.
 */
inline fun <T> runSuspendCatching(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
