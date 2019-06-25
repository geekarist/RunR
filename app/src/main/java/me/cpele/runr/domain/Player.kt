package me.cpele.runr.domain

import me.cpele.runr.model.data.PlaylistBo

interface Player {
    suspend fun play(playlist: PlaylistBo)
}