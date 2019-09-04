package me.cpele.runr.infra.viewmodel

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.cpele.runr.domain.usecase.*
import me.cpele.runr.infra.Event

class RunningViewModel(
    private val changePace: ChangePace,
    private val getPace: GetPace,
    private val observePlayerState: ObservePlayerState,
    private val waitForPlayer: WaitForPlayer,
    private val startRun: StartRun
) : ViewModel() {

    private val _effect = MutableLiveData<Event<Effect>>()
    val effect: LiveData<Event<Effect>> = _effect

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    init {

        // Initial view state
        _state.value = State()

        viewModelScope.launch {
            try {
                waitForPlayer.execute()

                // Ready
                _state.dispatchValue(
                    _state.value?.copy(
                        isChangePaceEnabled = true
                    )
                )

                // Display pace
                val response = getPace.execute()
                val newValueWithPace = _state.value?.copy(stepsPerMinText = response.paceStr)
                if (_state.value != newValueWithPace) {
                    _state.dispatchValue(newValueWithPace)
                }

                // Play music
                startRun.execute(response.pace)

                // Display player state
                val channel = observePlayerState.execute()
                for (playerState in channel) {
                    val newValueWithCover = _state.value?.copy(
                        coverUriStr = playerState.coverUrl,
                        progressVisibility = View.INVISIBLE,
                        coverVisibility = View.VISIBLE
                    )
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

        _state.dispatchLoading()

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
            noTrackVisibility = noTrackVisibility,
            progressVisibility = View.INVISIBLE
        )
        _state.dispatchValue(newValue)
    }

    private suspend fun MutableLiveData<State>.dispatchLoading() {
        dispatchValue(
            value?.copy(
                coverVisibility = View.INVISIBLE,
                noTrackVisibility = View.INVISIBLE,
                progressVisibility = View.VISIBLE,
                coverUriStr = null
            )
        )
    }

    data class State(
        val stepsPerMinText: String = "-",
        val coverUriStr: String? = null,
        val coverVisibility: Int = View.INVISIBLE,
        val noTrackVisibility: Int = View.INVISIBLE,
        val progressVisibility: Int = View.VISIBLE,
        val scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP,
        val isChangePaceEnabled: Boolean = false
    )

    sealed class Effect {
        data class Message(val message: String) : Effect()
    }
}

