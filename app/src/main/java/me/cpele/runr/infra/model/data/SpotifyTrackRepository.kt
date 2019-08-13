package me.cpele.runr.infra.model.data

import android.util.Log
import me.cpele.runr.domain.TokenProvider
import me.cpele.runr.domain.adapter.TrackRepository
import me.cpele.runr.domain.api.model.Track
import me.cpele.runr.infra.model.network.SpotifyService

class SpotifyTrackRepository(
    private val tokenProvider: TokenProvider,
    private val spotifyService: SpotifyService
) : TrackRepository {
    override suspend fun findByPace(pace: Int): List<Track> {
        val token = tokenProvider.get()
        Log.d(javaClass.simpleName, "Find tracks with pace: $pace")
        val recommendations = spotifyService.getRecommendations(
            authorization = "Bearer $token",
            minTempo = pace - 2,
            maxTempo = pace + 2,
            seedGenres = "guitar"
        )
        Log.d(
            javaClass.simpleName,
            "Found ${recommendations.tracks.size} tracks: $recommendations"
        )
        Log.d(javaClass.simpleName, "Spotify auth token: $token")
        return recommendations.tracks.map { Track(it.id) }
    }
}
