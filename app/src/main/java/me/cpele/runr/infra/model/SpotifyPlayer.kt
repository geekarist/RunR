package me.cpele.runr.infra.model

import android.app.Application
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.error.NotLoggedInException
import kotlinx.coroutines.suspendCancellableCoroutine
import me.cpele.runr.BuildConfig
import me.cpele.runr.R
import me.cpele.runr.domain.LoggedOutException
import me.cpele.runr.domain.MiscException
import me.cpele.runr.domain.bo.PlaylistBo
import me.cpele.runr.domain.iface.Player
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SpotifyPlayer(
    private val application: Application
) : Player {

    private var appRemote: SpotifyAppRemote? = null

    override suspend fun play(playlist: PlaylistBo) {
        Log.d(javaClass.simpleName, "Play ${playlist.id}")

        val appRemote = suspendCancellableCoroutine<SpotifyAppRemote> { continuation ->

            val params = ConnectionParams.Builder(BuildConfig.SPOTIFY_CLIENT_ID)
                .setRedirectUri(application.getString(R.string.conf_redirect_uri))
                .showAuthView(true)
                .build()

            SpotifyAppRemote.setDebugMode(BuildConfig.DEBUG)

            // TODO: Connect on start
            appRemote?.let { SpotifyAppRemote.disconnect(it) }
            appRemote = null
            SpotifyAppRemote.connect(application, params, object : Connector.ConnectionListener {

                override fun onFailure(throwable: Throwable?) {
                    continuation.resumeWithException(throwable.toCustomException())
                }

                override fun onConnected(remote: SpotifyAppRemote?) {
                    if (!continuation.isCompleted) {
                        appRemote = remote
                        continuation.resume(
                            remote
                                ?: throw Exception("App remote connection succeeded but remote is null")
                        )
                    }
                }
            })

            continuation.invokeOnCancellation { TODO() }
        }

        // TODO: Connect remote if not connected
        appRemote.playerApi.play("spotify:playlist:${playlist.id}")
    }

    private fun Throwable?.toCustomException(): Throwable =
        when (this) {
            is NotLoggedInException -> LoggedOutException(this)
            else -> MiscException(this)
        }
}
