package me.cpele.runr.infra.model.data

import android.util.Log
import me.cpele.runr.domain.TokenProvider
import me.cpele.runr.domain.bo.TrackBo
import me.cpele.runr.domain.iface.TrackRepository
import me.cpele.runr.infra.model.network.SpotifyService

class SpotifyTrackRepository(
    private val tokenProvider: TokenProvider,
    private val spotifyService: SpotifyService
) : TrackRepository {
    override suspend fun findByPace(pace: Int): List<TrackBo> {
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
            TrackBo(it.id, it.name, it.album.images[0].url)
        }
    }
}
