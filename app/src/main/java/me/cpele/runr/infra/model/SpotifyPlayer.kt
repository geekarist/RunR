package me.cpele.runr.infra.model

import android.util.Log
import me.cpele.runr.domain.bo.PlaylistBo
import me.cpele.runr.domain.iface.Player

class SpotifyPlayer(
    private val appRemoteProvider: SpotifyAppRemoteProvider
) : Player {
    override suspend fun play(playlist: PlaylistBo) {
        Log.d(javaClass.simpleName, "Play ${playlist.id}")
        val appRemote = appRemoteProvider.get()
        appRemote.playerApi.play("spotify:playlist:${playlist.id}")
    }

    override suspend fun subscribeToState(): Player.Subscription {
        val appRemote = appRemoteProvider.get()
        val subscription = appRemote.playerApi.subscribeToPlayerState()

        return object : Player.Subscription {
            override fun setEventCallback(callback: (Boolean) -> Boolean) {
                subscription.setEventCallback { callback(it.isPaused) }
            }

            override fun cancel() {
                subscription.cancel()
            }
        }
    }
}
