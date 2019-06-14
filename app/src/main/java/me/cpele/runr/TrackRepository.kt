package me.cpele.runr

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrackRepository(private val tokenProvider: TokenProvider) {
    suspend fun findByPace(pace: Int): List<TrackBo> {
        val token = tokenProvider.get()
        Log.d(javaClass.simpleName, "Find tracks with pace: $pace")
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.spotify.com")
            .build()
        val service = retrofit.create(SpotifyService::class.java)
        val recommendations = service.getRecommendations(
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
        return listOf()
    }
}
