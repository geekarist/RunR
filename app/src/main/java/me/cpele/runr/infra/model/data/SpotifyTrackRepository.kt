package me.cpele.runr.infra.model.data

import android.util.Log
import me.cpele.runr.domain.adapter.TrackRepository
import me.cpele.runr.domain.api.model.Track
import me.cpele.runr.domain.usecase.GetAuthToken
import me.cpele.runr.infra.model.network.SpotifyService

class SpotifyTrackRepository(
    private val getAuthToken: GetAuthToken,
    private val spotifyService: SpotifyService
) : TrackRepository {
    override suspend fun findByPace(pace: Int): List<Track> {
        val tokenResp = getAuthToken.execute().token
        Log.d(javaClass.simpleName, "Find tracks with pace: $pace")
        val recommendations = spotifyService.getRecommendations(
            authorization = "Bearer $tokenResp",
            minTempo = pace - 2,
            maxTempo = pace + 2,
            seedGenres = "guitar"
        )
        Log.d(
            javaClass.simpleName,
            "Found ${recommendations.tracks.size} tracks: $recommendations"
        )
        Log.d(javaClass.simpleName, "Spotify auth token: $tokenResp")
        return recommendations.tracks.map { Track(it.id) }
    }
}
