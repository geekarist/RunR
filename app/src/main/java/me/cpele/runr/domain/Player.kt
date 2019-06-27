package me.cpele.runr.domain

interface Player {
    suspend fun play(playlist: PlaylistBo)
}