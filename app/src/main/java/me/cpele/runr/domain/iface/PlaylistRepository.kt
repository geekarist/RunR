package me.cpele.runr.domain.iface

import me.cpele.runr.domain.bo.PlaylistBo
import me.cpele.runr.domain.bo.TrackBo

interface PlaylistRepository {
    suspend fun create(tracks: List<TrackBo>): PlaylistBo
}