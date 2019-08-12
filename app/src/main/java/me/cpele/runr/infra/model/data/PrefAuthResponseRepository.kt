package me.cpele.runr.infra.model.data

import android.app.Application
import android.preference.PreferenceManager
import com.google.gson.Gson
import me.cpele.runr.domain.entities.AuthResponse
import me.cpele.runr.domain.iface.AuthResponseRepository

class PrefAuthResponseRepository(
    private val gson: Gson,
    private val application: Application
) : AuthResponseRepository {

    override fun save(response: AuthResponse?) {
        val serialized = gson.toJson(response)
        PreferenceManager
            .getDefaultSharedPreferences(application)
            .edit()
            .putString("PREF_KEY_AUTH_RESPONSE", serialized)
            .apply()
    }

    override fun load(): AuthResponse? {
        val serialized = PreferenceManager
            .getDefaultSharedPreferences(application)
            .getString("PREF_KEY_AUTH_RESPONSE", null)
        return gson.fromJson(serialized, AuthResponse::class.java)
    }
}