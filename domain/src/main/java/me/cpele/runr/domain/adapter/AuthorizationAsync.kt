package me.cpele.runr.domain.adapter

import me.cpele.runr.domain.api.model.Auth

interface AuthorizationAsync {
    fun start(onReceiveResponse: (Auth?) -> Unit)
    fun cancel()
}
