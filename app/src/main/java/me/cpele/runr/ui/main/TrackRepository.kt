package me.cpele.runr.ui.main

import android.util.Log

class TrackRepository(val tokenProvider: TokenProvider) {
    fun findByPace(pace: Int): List<TrackBo> {
        val token = tokenProvider.get()
        Log.d(javaClass.simpleName, "Find tracks with pace $pace")
        Log.d(javaClass.simpleName, "Spotify auth token: $token")
        return listOf()
    }
}
