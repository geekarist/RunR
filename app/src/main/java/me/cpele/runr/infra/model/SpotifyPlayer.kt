package me.cpele.runr.infra.model

import android.app.Application
import android.graphics.Bitmap
import androidx.annotation.WorkerThread
import androidx.core.net.toUri
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.ImageUri
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import me.cpele.runr.BuildConfig
import me.cpele.runr.R
import me.cpele.runr.domain.TokenProvider
import me.cpele.runr.domain.bo.PlaylistBo
import me.cpele.runr.domain.iface.Player
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SpotifyPlayer(
    private val application: Application,
    private val tokenProvider: TokenProvider
) : Player, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private var appRemote: SpotifyAppRemote? = null

    override suspend fun play(playlist: PlaylistBo) {
        connect()
        startPlaying(playlist)
    }

    override suspend fun connect() {
        ensureUserConnected()
        ensureRemoteConnected()
    }

    private suspend fun ensureUserConnected() {
        tokenProvider.get() // Token not needed here
    }

    private suspend fun ensureRemoteConnected() =
        suspendCancellableCoroutine<Unit> { continuation ->

            SpotifyAppRemote.setDebugMode(BuildConfig.DEBUG)

            if (appRemote?.isConnected == true) {
                continuation.resume(Unit)
                return@suspendCancellableCoroutine
            }

            val params = ConnectionParams
                .Builder(BuildConfig.SPOTIFY_CLIENT_ID)
                .showAuthView(true)
                .setRedirectUri(application.getString(R.string.conf_redirect_uri))
                .build()

            SpotifyAppRemote.connect(application, params, object : Connector.ConnectionListener {
                override fun onFailure(p0: Throwable?) {
                    if (continuation.isActive) {
                        continuation.resumeWithException(
                            p0 ?: Exception("Unknown error connecting to Spotify app remote")
                        )
                    }
                }

                override fun onConnected(p0: SpotifyAppRemote?) {
                    appRemote = p0
                    continuation.resume(Unit)
                }
            })
        }

    private suspend fun startPlaying(playlist: PlaylistBo) {
        withContext(Dispatchers.IO) {
            appRemote?.playerApi
                ?.play("spotify:playlist:${playlist.id}")
                ?.await()
                ?: throw Exception("Error playing: API is null")
        }
    }

    override suspend fun state(): Player.State? =
        withContext(Dispatchers.IO) {
            appRemote
                ?.playerApi
                ?.playerState
                ?.await()
                ?.let { result ->
                    Player.State(
                        result.data.isPaused,
                        result.data.track.imageUri.raw,
                        result.error
                    )
                }
        }

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun observeStateForever(): ReceiveChannel<Player.State> =
        produce {
            withContext(Dispatchers.Main) { connect() }
            val subscription = appRemote?.playerApi?.subscribeToPlayerState()
            subscription?.setEventCallback {
                launch {
                    val url = withContext(Dispatchers.IO) { it.track.imageUri.persist() }
                    send(Player.State(it.isPaused, url, null))
                }
            }
            invokeOnClose { subscription?.cancel() }
            delay(Long.MAX_VALUE)
        }

    @WorkerThread
    private fun ImageUri.persist(): String {
        val dir = application.cacheDir
        val cacheFile = File(dir, "${raw}.wepb")
        if (!cacheFile.exists()) {
            val bitmap = appRemote?.imagesApi?.getImage(this, Image.Dimension.LARGE)?.await()?.data
            val output = cacheFile.outputStream()
            bitmap?.compress(Bitmap.CompressFormat.WEBP, 100, output)
            output.close()
        }
        return cacheFile.toUri().toString()
    }

    override fun disconnect() {
        appRemote?.let { SpotifyAppRemote.disconnect(it) }
        appRemote = null
        job.cancelChildren()
    }
}
