package me.cpele.runr.model

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import me.cpele.runr.view.SpotifyLoginActivity
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class TokenProvider(private val application: Application) {

    suspend fun get(): String = suspendCancellableCoroutine { continuation ->

        application.startActivity(
            Intent(
                application,
                SpotifyLoginActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )

        val broadcastManager = LocalBroadcastManager.getInstance(application)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val authResponse: AuthenticationResponse? =
                    intent?.getParcelableExtra(SpotifyLoginActivity.EXTRA_SPOTIFY_AUTH_RESPONSE)
                when {
                    authResponse?.accessToken != null ->
                        continuation.resume(authResponse.accessToken)
                    authResponse?.error != null -> {
                        val errorMsg = authResponse.error
                        val exception = Exception("Error: $errorMsg")
                        continuation.resumeWithException(exception)
                    }
                    else -> continuation.resumeWithException(
                        Exception("Auth response is null or empty")
                    )
                }
                broadcastManager.unregisterReceiver(this)
            }
        }

        val filter = IntentFilter(SpotifyLoginActivity.ACTION_SPOTIFY_LOGIN)
        broadcastManager.registerReceiver(receiver, filter)

        continuation.invokeOnCancellation { broadcastManager.unregisterReceiver(receiver) }
    }
}
