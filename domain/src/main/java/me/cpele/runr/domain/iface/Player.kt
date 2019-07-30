package me.cpele.runr.domain.iface

import kotlinx.coroutines.channels.ReceiveChannel
import me.cpele.runr.domain.bo.PlaylistBo

interface Player {

    suspend fun connect()
    suspend fun play(playlist: PlaylistBo)
    suspend fun state(): State?
    fun observeStateForever(): ReceiveChannel<State>
    fun disconnect()

    data class State(
        val isPaused: Boolean,
        val coverUrl: String,
        val error: Throwable?
    )
}