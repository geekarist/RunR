package me.cpele.runr.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import me.cpele.runr.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Handler().postDelayed({
            LocalBroadcastManager.getInstance(this).sendBroadcast(
                Intent("ACTION_SPOTIFY_LOGIN").putExtra(
                    "EXTRA_SPOTIFY_TOKEN",
                    "TODO"
                )
            )
            finish()
        }, 2000)
    }
}
