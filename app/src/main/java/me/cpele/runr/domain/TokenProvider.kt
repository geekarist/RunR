package me.cpele.runr.domain

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class TokenProvider(
    private val authorization: AuthorizationAsync
) {

    suspend fun get(): String = suspendCancellableCoroutine { continuation ->

        authorization.start {
            when {
                it?.accessToken != null -> continuation.resume(it.accessToken)
                it?.error != null -> continuation.resumeWithException(Exception("Error: $it"))
                else -> {
                    val exception = Exception("Auth response is null or empty")
                    continuation.resumeWithException(exception)
                }
            }
        }

        continuation.invokeOnCancellation { authorization.cancel() }
    }
}
