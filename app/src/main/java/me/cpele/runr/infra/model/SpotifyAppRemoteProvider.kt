package me.cpele.runr.infra.model

import android.content.Context
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import me.cpele.runr.BuildConfig
import me.cpele.runr.R
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SpotifyAppRemoteProvider(private val context: Context) {

    private var spotifyAppRemote: SpotifyAppRemote? = null

    suspend fun get() = spotifyAppRemote ?: connect()

    private suspend fun connect(): SpotifyAppRemote = suspendCoroutine { continuation ->

        val params = ConnectionParams.Builder(BuildConfig.SPOTIFY_CLIENT_ID)
            .setRedirectUri(context.getString(R.string.conf_redirect_uri))
            .showAuthView(true)
            .build()

        SpotifyAppRemote.setDebugMode(BuildConfig.DEBUG)
        SpotifyAppRemote.connect(context, params, object : Connector.ConnectionListener {

            override fun onFailure(throwable: Throwable?) {
                continuation.resumeWithException(
                    Exception(
                        "App remote connection failed",
                        throwable
                    )
                )
            }

            override fun onConnected(remote: SpotifyAppRemote?) {
                continuation.resume(
                    remote
                        ?: throw Exception("App remote connection succeeded but remote is null")
                )
            }
        })
    }
}
