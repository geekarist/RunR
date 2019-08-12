package me.cpele.runr.domain.iface

import kotlinx.coroutines.channels.ReceiveChannel
import me.cpele.runr.domain.entities.Playlist

interface Player {

    suspend fun connect()
    suspend fun play(playlist: Playlist)
    suspend fun state(): State?
    fun observeStateForever(): ReceiveChannel<State>
    fun disconnect()

    data class State(
        val isPaused: Boolean,
        val coverUrl: String,
        val error: Throwable?
    )
}