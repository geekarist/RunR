package me.cpele.runr.infra.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.cpele.runr.R
import me.cpele.runr.domain.usecase.DecreasePace
import me.cpele.runr.domain.usecase.GetPace
import me.cpele.runr.domain.usecase.IncreasePace
import me.cpele.runr.domain.usecase.ObservePlayerState
import me.cpele.runr.getUrl

class RunningViewModel(
    private val increasePace: IncreasePace,
    private val getPace: GetPace,
    private val decreasePace: DecreasePace,
    private val observePlayerState: ObservePlayerState,
    private val application: Application
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    data class State(
        val stepsPerMinText: String = "-",
        val coverUriStr: String,
        val coverVisibility: Int = View.VISIBLE,
        val noTrackVisibility: Int = View.INVISIBLE
    )

    init {
        viewModelScope.launch {
            val response = getPace.execute()
            val previousValue = _state.value
                ?: State(coverUriStr = application.getUrl(R.drawable.cover_placeholder))
            val newValueWithPace = previousValue.copy(stepsPerMinText = response.paceStr)
            if (_state.value != newValueWithPace) {
                withContext(Dispatchers.Main) { _state.value = newValueWithPace }
            }

            val channel = observePlayerState.execute()
            for (playerState in channel) {
                Log.d(
                    "COVER_LOAD",
                    "VM receives player state with cover: ${playerState.coverUrl}"
                )
                val newValueWithCover = _state.value?.copy(coverUriStr = playerState.coverUrl)
                if (_state.value != newValueWithCover) {
                    Log.d(
                        "COVER_LOAD",
                        "VM detects cover change, emitting value"
                    )
                    withContext(Dispatchers.Main) { _state.value = newValueWithCover }
                }
            }
        }
    }

    fun onIncreasePace() = viewModelScope.launch {
        val response = increasePace.execute()

        val coverVisibility: Int
        val noTrackVisibility: Int
        when (response) {
            is IncreasePace.Response.Success -> {
                coverVisibility = View.VISIBLE
                noTrackVisibility = View.INVISIBLE
            }
            is IncreasePace.Response.NoTrackFound -> {
                coverVisibility = View.INVISIBLE
                noTrackVisibility = View.VISIBLE
            }
        }

        val oldValue = _state.value
        val newValue = oldValue?.copy(
            stepsPerMinText = response.newPaceStr,
            coverVisibility = coverVisibility,
            noTrackVisibility = noTrackVisibility
        )
        withContext(Dispatchers.Main) { _state.value = newValue }
    }

    fun onDecreasePace() = viewModelScope.launch {
        // TODO: Empty state
        val response = decreasePace.execute()
        val newValue = _state.value?.copy(stepsPerMinText = response.newPaceStr)
        withContext(Dispatchers.Main) { _state.value = newValue }
    }
}
