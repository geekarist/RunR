package me.cpele.runr

import android.util.Log

class Player(private val appRemoteProvider: SpotifyAppRemoteProvider) {
    suspend fun play(playlist: PlaylistBo) {
        Log.d(javaClass.simpleName, "Play ${playlist.id}")
        val appRemote = appRemoteProvider.get()
        appRemote.playerApi.play("spotify:playlist:${playlist.id}")
    }
}
