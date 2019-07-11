package me.cpele.runr.domain.iface

import me.cpele.runr.domain.bo.PlaylistBo

interface Player {
    suspend fun play(playlist: PlaylistBo)
    suspend fun subscribeToState(): Subscription
    suspend fun ensureReady()

    interface Subscription {
        fun setEventCallback(callback: (Boolean) -> Boolean)
        fun cancel()
    }
}