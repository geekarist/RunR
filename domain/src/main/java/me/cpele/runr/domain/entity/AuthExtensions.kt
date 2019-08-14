package me.cpele.runr.domain.entity

import me.cpele.runr.domain.api.model.Auth
import java.util.*
import java.util.concurrent.TimeUnit

internal val Auth.isNotExpired: Boolean
    get() {
        val issueDateMs = issueDate.time
        val expiresInMs = TimeUnit.SECONDS.toMillis(expiresInSec.toLong())
        val expirationDate = Date(issueDateMs + expiresInMs)
        return Date().before(expirationDate)
    }