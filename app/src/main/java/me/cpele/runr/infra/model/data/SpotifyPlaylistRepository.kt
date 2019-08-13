package me.cpele.runr.infra.model.data

import android.util.Log
import me.cpele.runr.domain.adapter.PlaylistRepository
import me.cpele.runr.domain.api.model.Playlist
import me.cpele.runr.domain.api.model.Track
import me.cpele.runr.domain.usecase.GetAuth
import me.cpele.runr.infra.model.network.SpotifyPlaylistCreateRequest
import me.cpele.runr.infra.model.network.SpotifyService
import me.cpele.runr.infra.model.network.SpotifyTracksPostRequest
import java.util.*

class SpotifyPlaylistRepository(
    private val spotifyService: SpotifyService,
    private val getAuth: GetAuth
) : PlaylistRepository {
    override suspend fun create(tracks: List<Track>): Playlist {
        val tokenResponse = getAuth.execute()
        val authorization = "Bearer ${tokenResponse.token}"

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

        return Playlist(spotifyPlaylist.id)
    }
}
