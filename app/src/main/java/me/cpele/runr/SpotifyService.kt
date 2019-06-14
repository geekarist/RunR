package me.cpele.runr

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyService {
    @GET("/v1/recommendations")
    suspend fun getRecommendations(
        @Header("Authorization") authorization: String,
        @Query("min_tempo") minTempo: Int,
        @Query("max_tempo") maxTempo: Int,
        @Query("seed_genres") seedGenres: String
    ): SpotifyRecommendations
}
