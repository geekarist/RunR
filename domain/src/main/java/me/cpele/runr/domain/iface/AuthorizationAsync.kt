package me.cpele.runr.domain.iface

import me.cpele.runr.domain.bo.AuthResponseBo

interface AuthorizationAsync {
    fun start(onReceiveResponse: (AuthResponseBo?) -> Unit)
    fun cancel()
}
