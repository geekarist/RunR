package me.cpele.runr.domain.usecase

import kotlinx.coroutines.suspendCancellableCoroutine
import me.cpele.runr.domain.adapter.AuthResponseRepository
import me.cpele.runr.domain.adapter.AuthorizationAsync
import me.cpele.runr.domain.entity.isNotExpired
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GetAuth(
    private val authorization: AuthorizationAsync,
    private val authResponseRepository: AuthResponseRepository
) {

    suspend fun execute(): Response {

        val savedToken = authResponseRepository.load()
        if (savedToken?.isNotExpired == true) {
            return Response(
                savedToken.accessToken
                    ?: throw IllegalStateException("Access token is not expired and should be set")
            )

        }

        return suspendCancellableCoroutine { continuation ->

            authorization.start {
                authResponseRepository.save(it)
                when {
                    it?.accessToken != null -> continuation.resume(
                        Response(
                            it.accessToken
                        )
                    )
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

    data class Response(val token: String)
}
