package me.cpele.runr.domain

interface PlaylistRepository {
    suspend fun create(tracks: List<TrackBo>): PlaylistBo
}