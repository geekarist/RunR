package me.cpele.runr.infra.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import me.cpele.runr.BuildConfig
import me.cpele.runr.R

class SpotifyLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val clientId = BuildConfig.SPOTIFY_CLIENT_ID
        val redirectUri = getString(R.string.conf_redirect_uri)
        val scopes = arrayOf("user-read-private", "app-remote-control", "playlist-modify-public")
        val request = AuthenticationRequest.Builder(
            clientId,
            AuthenticationResponse.Type.TOKEN,
            redirectUri
        ).setScopes(scopes).build()
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE_LOGIN, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_LOGIN) {
            val authResponse = AuthenticationClient.getResponse(resultCode, data)
            sendAuthStateThenFinish(authResponse)
        }
    }

    private fun sendAuthStateThenFinish(authResponse: AuthenticationResponse?) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(
            Intent(ACTION_SPOTIFY_LOGIN).putExtra(
                EXTRA_SPOTIFY_AUTH_RESPONSE,
                authResponse
            )
        )
        finish()
    }

    companion object {
        private const val REQUEST_CODE_LOGIN = 42
        const val ACTION_SPOTIFY_LOGIN = "ACTION_SPOTIFY_LOGIN"
        const val EXTRA_SPOTIFY_AUTH_RESPONSE = "EXTRA_SPOTIFY_AUTH_RESPONSE"
    }
}
