package me.cpele.runr.domain.iface

import me.cpele.runr.domain.bo.PlaylistBo

interface Player {
    suspend fun play(playlist: PlaylistBo)
    suspend fun subscribeToState(): Subscription
    suspend fun connect()
    fun disconnect()

    interface Subscription {
        fun setEventCallback(callback: (Boolean) -> Boolean)
        fun cancel()
    }
}