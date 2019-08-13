package me.cpele.runr.domain.api.model

import java.util.*

data class Auth(
    val accessToken: String? = null,
    val error: String?,
    val issueDate: Date,
    val expiresInSec: Int
)
