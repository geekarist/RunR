package me.cpele.runr.ui.main

import android.util.Log

class TrackRepository {
    fun findByPace(pace: Int): List<TrackBo> {
        Log.d(javaClass.simpleName, "Find tracks with pace $pace")
        return listOf()
    }
}
