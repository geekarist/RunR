package me.cpele.runr.domain.adapter

import kotlinx.coroutines.channels.ReceiveChannel
import me.cpele.runr.domain.api.model.Playlist

interface Player {

    val isInstalled: Boolean
    val isConnected: Boolean
    suspend fun isPlaying(): Boolean

    suspend fun connect()
    suspend fun play(playlist: Playlist)
    fun observeStateForever(): ReceiveChannel<State>
    fun disconnect()
    suspend fun install()
    suspend fun pause()

    data class State(
        val isPaused: Boolean,
        val coverUrl: String,
        val error: Throwable?,
        val trackArtist: CharSequence,
        val trackTitle: CharSequence
    )
}