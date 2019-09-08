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
import me.cpele.runr.domain.adapter.Player
import me.cpele.runr.domain.api.model.Playlist
import me.cpele.runr.domain.usecase.GetAuth
import java.io.File
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SpotifyPlayer(
    private val application: Application,
    private val getAuth: GetAuth
) : Player, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default
    private val persistJob = Job()

    private val persistDispatch = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val persistContext = persistJob + persistDispatch
    private val persistScope = CoroutineScope(persistContext)
    private var appRemote: SpotifyAppRemote? = null

    private var _isConnected: Boolean = false
    override val isConnected: Boolean
        get() = _isConnected

    override suspend fun play(playlist: Playlist) {
        startPlaying(playlist)
    }

    override suspend fun connect() {
        ensureUserConnected()
        ensureRemoteConnected()
        _isConnected = true
    }

    private suspend fun ensureUserConnected() {
        getAuth.execute() // Token not needed here
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

            // TODO: Create sequential version of SpotifyAppRemote
            // TODO: Create AppRemote interface
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

    private suspend fun startPlaying(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            appRemote?.playerApi
                ?.play("spotify:playlist:${playlist.id}")
                ?.await()
                ?: throw Exception("Error playing: API is null")
        }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun observeStateForever(): ReceiveChannel<Player.State> =
        produce {
            val subscription = appRemote?.playerApi?.subscribeToPlayerState()
            subscription?.setEventCallback { playerState ->
                playerState?.track ?: return@setEventCallback

                // Launch on single thread to prevent concurrency issues
                persistScope.launch {
                    val url = playerState.track.imageUri.persist()
                    send(
                        Player.State(
                            playerState.isPaused,
                            url,
                            null,
                            playerState.track.artist.name,
                            playerState.track.name
                        )
                    )
                }
            }
            invokeOnClose { subscription?.cancel() }
            delay(Long.MAX_VALUE)
        }

    @WorkerThread
    private fun ImageUri.persist(): String {
        val dir = application.cacheDir
        val cacheFile = File(dir, "$raw.wepb")
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
        persistJob.cancelChildren()
        _isConnected = false
    }
}
