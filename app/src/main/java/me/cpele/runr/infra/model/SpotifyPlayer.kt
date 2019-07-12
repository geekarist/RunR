package me.cpele.runr.infra.model

import android.util.Log
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.suspendCancellableCoroutine
import me.cpele.runr.domain.LoggedOutException
import me.cpele.runr.domain.TokenProvider
import me.cpele.runr.domain.bo.PlaylistBo
import me.cpele.runr.domain.iface.Player
import kotlin.coroutines.resume

class SpotifyPlayer(
    private val appRemoteProvider: SpotifyAppRemoteProvider,
    private val tokenProvider: TokenProvider
) : Player {

    override suspend fun connect() {
        val loggedIn = try {
            val appRemote = appRemoteProvider.get()
            isLoggedIn(appRemote)
        } catch (e: LoggedOutException) {
            false
        }
        if (!loggedIn) tokenProvider.forceRefresh()
    }

    override fun disconnect() {
        appRemoteProvider.disconnect()
    }

    private suspend fun isLoggedIn(appRemote: SpotifyAppRemote): Boolean =
        suspendCancellableCoroutine { continuation ->
            val subscription = appRemote.userApi.subscribeToUserStatus()
            continuation.invokeOnCancellation { subscription.cancel() }
            subscription.setEventCallback {
                continuation.resume(it.isLoggedIn)
                subscription.cancel()
            }
        }

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
