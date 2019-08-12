package me.cpele.runr.domain.iface

import me.cpele.runr.domain.entities.Playlist
import me.cpele.runr.domain.entities.Track

interface PlaylistRepository {
    suspend fun create(tracks: List<Track>): Playlist
}