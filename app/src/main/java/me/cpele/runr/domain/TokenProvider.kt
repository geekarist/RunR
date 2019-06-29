package me.cpele.runr.domain

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class TokenProvider(
    private val authorization: AuthorizationAsync,
    private val authResponseRepository: AuthResponseRepository
) {

    suspend fun get(): String {

        val savedToken = authResponseRepository.load()
        if (savedToken?.isNotExpired == true) {
            return savedToken.accessToken
                ?: throw IllegalStateException("Access token is not expired and should be set")
        }

        return suspendCancellableCoroutine { continuation ->

            authorization.start {
                authResponseRepository.save(it)
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
}
