package me.cpele.runr.domain.iface

import me.cpele.runr.domain.entities.AuthResponse

interface AuthorizationAsync {
    fun start(onReceiveResponse: (AuthResponse?) -> Unit)
    fun cancel()
}
