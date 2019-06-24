package me.cpele.runr.model.data

import android.util.Log
import me.cpele.runr.model.TokenProvider
import me.cpele.runr.model.network.SpotifyService

class TrackRepository(
    private val tokenProvider: TokenProvider,
    private val spotifyService: SpotifyService
) {
    suspend fun findByPace(pace: Int): List<TrackBo> {
        val token = tokenProvider.get()
        Log.d(javaClass.simpleName, "Find tracks with pace: $pace")
        val recommendations = spotifyService.getRecommendations(
            authorization = "Bearer $token",
            minTempo = pace - 2,
            maxTempo = pace + 2,
            seedGenres = "rock"
        )
        Log.d(
            javaClass.simpleName,
            "Found ${recommendations.tracks.size} tracks: $recommendations"
        )
        Log.d(javaClass.simpleName, "Spotify auth token: $token")
        return recommendations.tracks.map {
            TrackBo(it.id, it.name)
        }
    }
}