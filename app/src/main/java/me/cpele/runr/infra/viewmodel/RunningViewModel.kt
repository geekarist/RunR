package me.cpele.runr.infra.viewmodel

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.cpele.runr.R
import me.cpele.runr.domain.usecase.ChangePace
import me.cpele.runr.domain.usecase.GetPace
import me.cpele.runr.domain.usecase.ObservePlayerState
import me.cpele.runr.getUrl
import me.cpele.runr.infra.Event

class RunningViewModel(
    private val changePace: ChangePace,
    private val getPace: GetPace,
    private val observePlayerState: ObservePlayerState,
    private val application: Application
) : ViewModel() {

    private val _effect = MutableLiveData<Event<Effect>>()
    val effect: LiveData<Event<Effect>> = _effect

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    init {
        onInit()
    }

    fun onInit() {
        _state.value = State(
            coverUriStr = application.getUrl(R.drawable.cover_placeholder)
        )

        viewModelScope.launch {
            try {
                val response = getPace.execute()
                val previousValue = _state.value
                val newValueWithPace = previousValue?.copy(stepsPerMinText = response.paceStr)
                if (_state.value != newValueWithPace) {
                    _state.dispatchValue(newValueWithPace)
                }

                val channel = observePlayerState.execute()
                for (playerState in channel) {
                    val newValueWithCover = _state.value?.copy(coverUriStr = playerState.coverUrl)
                    if (_state.value != newValueWithCover) {
                        _state.dispatchValue(newValueWithCover)
                    }
                }
            } catch (e: Exception) {
                val newValue: Event<Effect> = Event(Effect.Message("Error: ${e.message}"))
                _effect.dispatchValue(newValue)
                Log.w(javaClass.simpleName, e)
            }
        }
    }

    fun onIncreasePace() = viewModelScope.launch {
        onChangePace(ChangePace.Direction.Increase)
    }

    fun onDecreasePace() = viewModelScope.launch {
        onChangePace(ChangePace.Direction.Decrease)
    }

    private suspend fun onChangePace(direction: ChangePace.Direction) {
        val response = changePace.execute(direction)

        val coverVisibility: Int
        val noTrackVisibility: Int
        when (response) {
            is ChangePace.Response.Success -> {
                coverVisibility = View.VISIBLE
                noTrackVisibility = View.INVISIBLE
            }
            is ChangePace.Response.NoTrackFound -> {
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
        _state.dispatchValue(newValue)
    }

    fun onOrientationChanged(orientation: Int?) {
        val scaleType: ImageView.ScaleType =
            when (orientation) {
                Configuration.ORIENTATION_PORTRAIT -> ImageView.ScaleType.FIT_START
                else -> ImageView.ScaleType.FIT_CENTER
            }
        _state.value = _state.value?.copy(scaleType = scaleType)
    }

    data class State(
        val stepsPerMinText: String = "-",
        val coverUriStr: String,
        val coverVisibility: Int = View.VISIBLE,
        val noTrackVisibility: Int = View.INVISIBLE,
        val scaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_START
    )

    sealed class Effect {
        data class Message(val message: String) : Effect()
    }
}

