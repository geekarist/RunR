package me.cpele.runr.domain

import me.cpele.runr.model.data.TrackBo

interface TrackRepository {
    suspend fun findByPace(pace: Int): List<TrackBo>
}