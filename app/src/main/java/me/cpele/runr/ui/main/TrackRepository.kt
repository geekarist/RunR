package me.cpele.runr.ui.main

import android.util.Log

class TrackRepository(private val tokenProvider: TokenProvider) {
    suspend fun findByPace(pace: Int): List<TrackBo> {
        val token = tokenProvider.get()
        Log.d(javaClass.simpleName, "Find tracks with pace: $pace")
        Log.d(javaClass.simpleName, "Spotify auth token: $token")
        return listOf()
    }
}
