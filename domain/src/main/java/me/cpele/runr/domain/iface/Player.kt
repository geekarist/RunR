package me.cpele.runr.domain.iface

import me.cpele.runr.domain.bo.PlaylistBo

interface Player {

    val state: State?

    suspend fun connect()
    suspend fun play(playlist: PlaylistBo)
    fun disconnect()

    data class State(
        val isPaused: Boolean,
        val coverUrl: String,
        val error: Throwable
    )
}