package me.cpele.runr.infra.model

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.spotify.sdk.android.authentication.AuthenticationResponse
import me.cpele.runr.domain.entities.AuthResponse
import me.cpele.runr.domain.iface.AuthorizationAsync
import me.cpele.runr.infra.view.SpotifyLoginActivity
import java.util.*

class SpotifyAuthorizationAsync(
    private val application: Application
) : AuthorizationAsync {

    private var receiver: BroadcastReceiver? = null

    override fun start(onReceiveResponse: (AuthResponse?) -> Unit) {
        application.startActivity(
            Intent(
                application,
                SpotifyLoginActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )

        val broadcastManager = LocalBroadcastManager.getInstance(application)
        cancel()
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val authResponse: AuthenticationResponse? =
                    intent?.getParcelableExtra(SpotifyLoginActivity.EXTRA_SPOTIFY_AUTH_RESPONSE)
                onReceiveResponse(authResponse?.toBo())
                broadcastManager.unregisterReceiver(this)
            }
        }

        val filter = IntentFilter(SpotifyLoginActivity.ACTION_SPOTIFY_LOGIN)
        receiver?.let { broadcastManager.registerReceiver(it, filter) }
    }

    override fun cancel() {
        val broadcastManager = LocalBroadcastManager.getInstance(application)
        receiver?.let { broadcastManager.unregisterReceiver(it) }
    }

    private fun AuthenticationResponse.toBo(): AuthResponse? =
        AuthResponse(
            accessToken = accessToken,
            error = error,
            issueDate = Date(),
            expiresInSec = expiresIn
        )
}
