package me.cpele.runr.domain

data class AuthResponseBo(
    val accessToken: String? = null,
    val error: String?
)
