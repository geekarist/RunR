package me.cpele.runr.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import me.cpele.runr.BuildConfig
import me.cpele.runr.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val clientId = BuildConfig.SPOTIFY_CLIENT_ID
        val redirectUri = "me.cpele.runr://spotify-redirect"
        val scopes = arrayOf("user-read-private", "app-remote-control")
        val request = AuthenticationRequest.Builder(
            clientId,
            AuthenticationResponse.Type.TOKEN,
            redirectUri
        ).setScopes(scopes).build()
        AuthenticationClient.openLoginActivity(this, 42, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 42) {
            val authResponse = AuthenticationClient.getResponse(resultCode, data)
            when (authResponse.type) {
                AuthenticationResponse.Type.TOKEN -> sendTokenThenFinish(authResponse.accessToken)
                AuthenticationResponse.Type.ERROR -> sendErrorThenFinish(authResponse.error)
                else -> TODO()
            }
        }
    }

    private fun sendErrorThenFinish(error: String?) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(
            Intent("ACTION_SPOTIFY_LOGIN").putExtra(
                "EXTRA_SPOTIFY_ERROR",
                error
            )
        )
        finish()
    }

    private fun sendTokenThenFinish(token: String) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(
            Intent("ACTION_SPOTIFY_LOGIN").putExtra(
                "EXTRA_SPOTIFY_TOKEN",
                token
            )
        )
        finish()
    }
}
