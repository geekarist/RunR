package me.cpele.runr.infra.model.network

import retrofit2.http.*

interface SpotifyService {
    @GET("/v1/recommendations")
    suspend fun getRecommendations(
        @Header("Authorization") authorization: String,
        @Query("min_tempo") minTempo: Int,
        @Query("max_tempo") maxTempo: Int,
        @Query("seed_genres") seedGenres: String
    ): SpotifyRecommendations

    @GET("https://api.spotify.com/v1/me")
    suspend fun getCurrentUserProfile(
        @Header("Authorization") authorization: String
    ): SpotifyUser

    @POST("/v1/users/{user_id}/playlists")
    suspend fun postPlaylist(
        @Header("Authorization") authorization: String,
        @Path("user_id") userId: String,
        @Body request: SpotifyPlaylistCreateRequest
    ): SpotifyPlaylist

    @POST("/v1/playlists/{playlist_id}/tracks")
    suspend fun postTracks(
        @Header("Authorization") authorization: String,
        @Path("playlist_id") playlistId: String,
        @Body uris: SpotifyTracksPostRequest
    )
}
