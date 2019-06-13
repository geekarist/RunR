package me.cpele.runr.ui.main

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class TokenProvider(private val application: Application) {

    suspend fun get(): String = suspendCancellableCoroutine { continuation ->

        application.startActivity(Intent(application, LoginActivity::class.java))

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when {
                    intent?.hasExtra("EXTRA_SPOTIFY_TOKEN") == true ->
                        continuation.resume(intent.getStringExtra("EXTRA_SPOTIFY_TOKEN"))
                    intent?.hasExtra("EXTRA_SPOTIFY_ERROR") == true -> {
                        val errorMsg = intent.getStringExtra("EXTRA_SPOTIFY_ERROR")
                        val exception = Exception("Error: $errorMsg")
                        continuation.resumeWithException(exception)
                    }
                    else -> continuation.resumeWithException(Exception())
                }
            }
        }

        val broadcastManager = LocalBroadcastManager.getInstance(application)
        val filter = IntentFilter("ACTION_SPOTIFY_LOGIN")
        broadcastManager.registerReceiver(receiver, filter)

        continuation.invokeOnCancellation { broadcastManager.unregisterReceiver(receiver) }
    }
}
