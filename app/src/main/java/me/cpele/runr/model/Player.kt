package me.cpele.runr.model

import android.util.Log
import me.cpele.runr.model.data.PlaylistBo

class Player(private val appRemoteProvider: SpotifyAppRemoteProvider) {
    suspend fun play(playlist: PlaylistBo) {
        Log.d(javaClass.simpleName, "Play ${playlist.id}")
        val appRemote = appRemoteProvider.get()
        appRemote.playerApi.play("spotify:playlist:${playlist.id}")
    }
}
