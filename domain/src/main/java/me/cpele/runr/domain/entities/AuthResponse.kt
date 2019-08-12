package me.cpele.runr.domain.entities

import java.util.*
import java.util.concurrent.TimeUnit

data class AuthResponse(
    val accessToken: String? = null,
    val error: String?,
    val issueDate: Date,
    val expiresInSec: Int
) {
    val isNotExpired: Boolean
        get() {
            val issueDateMs = issueDate.time
            val expiresInMs = TimeUnit.SECONDS.toMillis(expiresInSec.toLong())
            val expirationDate = Date(issueDateMs + expiresInMs)
            return Date().before(expirationDate)
        }
}