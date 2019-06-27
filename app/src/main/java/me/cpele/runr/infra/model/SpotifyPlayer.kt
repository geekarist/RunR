package me.cpele.runr.infra.model

import android.util.Log
import me.cpele.runr.domain.Player
import me.cpele.runr.domain.PlaylistBo

class SpotifyPlayer(private val appRemoteProvider: SpotifyAppRemoteProvider) : Player {
    override suspend fun play(playlist: PlaylistBo) {
        Log.d(javaClass.simpleName, "Play ${playlist.id}")
        val appRemote = appRemoteProvider.get()
        appRemote.playerApi.play("spotify:playlist:${playlist.id}")
    }
}
