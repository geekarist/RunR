package me.cpele.runr.domain.adapter

import me.cpele.runr.domain.api.model.Track

interface TrackRepository {
    suspend fun findByPace(pace: Int): List<Track>
}