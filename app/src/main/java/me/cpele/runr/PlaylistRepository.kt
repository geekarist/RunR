package me.cpele.runr

import android.util.Log
import java.util.*

class PlaylistRepository(
    private val spotifyService: SpotifyService,
    private val tokenProvider: TokenProvider
) {
    suspend fun create(tracks: List<TrackBo>): PlaylistBo {
        val token = tokenProvider.get()
        val authorization = "Bearer $token"

        Log.d(javaClass.simpleName, "Get user id")
        val profile = spotifyService.getCurrentUserProfile(authorization = authorization)
        val userId = profile.id
        Log.d(javaClass.simpleName, "User ID is $userId")

        Log.d(javaClass.simpleName, "Insert tracks")
        val spotifyPlaylist = spotifyService.postPlaylist(
            authorization = authorization,
            userId = userId,
            request = SpotifyPlaylistCreateRequest(name = UUID.randomUUID().toString())
        )

        Log.d(javaClass.simpleName, "Add tracks to tracks")
        spotifyService.postTracks(
            authorization = authorization,
            playlistId = spotifyPlaylist.id,
            uris = SpotifyTracksPostRequest(tracks.map { "spotify:track:${it.id}" })
        )

        return PlaylistBo(spotifyPlaylist.id, tracks)
    }
}
