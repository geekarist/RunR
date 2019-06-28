package me.cpele.runr.domain

interface AuthorizationAsync {
    fun start(onReceiveResponse: (AuthResponseBo?) -> Unit)
    fun cancel()
}
