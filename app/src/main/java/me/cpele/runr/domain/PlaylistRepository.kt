package me.cpele.runr.domain

import me.cpele.runr.model.data.PlaylistBo
import me.cpele.runr.model.data.TrackBo

interface PlaylistRepository {
    suspend fun create(tracks: List<TrackBo>): PlaylistBo
}