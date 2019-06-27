package me.cpele.runr.infra.model.data

import android.util.Log
import me.cpele.runr.domain.PlaylistBo
import me.cpele.runr.domain.PlaylistRepository
import me.cpele.runr.domain.TokenProvider
import me.cpele.runr.domain.TrackBo
import me.cpele.runr.infra.model.network.SpotifyPlaylistCreateRequest
import me.cpele.runr.infra.model.network.SpotifyService
import me.cpele.runr.infra.model.network.SpotifyTracksPostRequest
import java.util.*

class SpotifyPlaylistRepository(
    private val spotifyService: SpotifyService,
    private val tokenProvider: TokenProvider
) : PlaylistRepository {
    override suspend fun create(tracks: List<TrackBo>): PlaylistBo {
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
