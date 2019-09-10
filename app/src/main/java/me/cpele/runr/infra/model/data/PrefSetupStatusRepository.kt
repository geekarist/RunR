package me.cpele.runr.infra.model.data

import android.app.Application
import android.preference.PreferenceManager
import me.cpele.runr.domain.adapter.SetupStatusRepository

class PrefSetupStatusRepository(private val application: Application) : SetupStatusRepository {

    private val preferences by lazy { PreferenceManager.getDefaultSharedPreferences(application) }

    override var value: SetupStatusRepository.Status
        get() {
            val defaultStatus = SetupStatusRepository.Status.TODO
            val statusName = preferences.getString(
                KEY_SETUP_STATUS,
                defaultStatus.name
            ) ?: defaultStatus.name
            return SetupStatusRepository.Status.valueOf(statusName)
        }
        set(value) {
            preferences.edit().putString(KEY_SETUP_STATUS, value.name).apply()
        }

    companion object {
        const val KEY_SETUP_STATUS = "KEY_SETUP_STATUS"
    }
}