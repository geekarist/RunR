package me.cpele.runr.domain.iface

import me.cpele.runr.domain.bo.TrackBo

interface TrackRepository {
    suspend fun findByPace(pace: Int): List<TrackBo>
}