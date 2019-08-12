package me.cpele.runr.domain.iface

import me.cpele.runr.domain.entities.Track

interface TrackRepository {
    suspend fun findByPace(pace: Int): List<Track>
}