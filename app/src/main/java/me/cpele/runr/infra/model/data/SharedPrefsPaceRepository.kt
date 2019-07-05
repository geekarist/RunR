package me.cpele.runr.infra.model.data

import android.app.Application
import android.preference.PreferenceManager
import me.cpele.runr.domain.iface.PaceRepository

class SharedPrefsPaceRepository(private val app: Application) :
    PaceRepository {

    override fun get(): Int {
        return PreferenceManager.getDefaultSharedPreferences(app).getInt(PREF_PACE, DEFAULT_PACE)
    }

    override fun set(value: Int) {
        PreferenceManager.getDefaultSharedPreferences(app).edit().putInt(PREF_PACE, value).apply()
    }

    companion object {
        private const val PREF_PACE = "PREF_PACE"
        private const val DEFAULT_PACE = 140
    }
}