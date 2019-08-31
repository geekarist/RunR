package me.cpele.runr.domain.adapter

import kotlinx.coroutines.channels.ReceiveChannel
import me.cpele.runr.domain.api.model.Playlist

interface Player {

    val isConnected: Boolean

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