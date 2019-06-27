package me.cpele.runr.domain

interface TrackRepository {
    suspend fun findByPace(pace: Int): List<TrackBo>
}