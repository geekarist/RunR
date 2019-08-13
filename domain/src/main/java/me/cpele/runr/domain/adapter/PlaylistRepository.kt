package me.cpele.runr.domain.adapter

import me.cpele.runr.domain.api.model.Playlist
import me.cpele.runr.domain.api.model.Track

interface PlaylistRepository {
    suspend fun create(tracks: List<Track>): Playlist
}