package me.cpele.runr.infra.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.cpele.runr.domain.usecase.CheckSetup
import me.cpele.runr.domain.usecase.ConnectPlayer
import me.cpele.runr.domain.usecase.InstallPlayer
import me.cpele.runr.infra.Event

class CheckSetupViewModel(
    private val checkSetup: CheckSetup,
    private val installPlayer: InstallPlayer,
    private val connectPlayer: ConnectPlayer
) : ViewModel() {

    private val _effect = MutableLiveData<Event<Effect>>()

    val effect: LiveData<Event<Effect>> get() = _effect
    private val _state = MutableLiveData<State>()

    val state: LiveData<State> get() = _state

    private var installJob: Job? = null

    init {
        viewModelScope.launch {
            _state.dispatchValue(State())
            check()
        }
    }

    private suspend fun check() {
        val response = checkSetup.execute()
        when (response.status) {
            CheckSetup.Status.CHECK_ALREADY_DONE -> _effect.dispatchValue(Event(Effect.SetupCompleted))
            CheckSetup.Status.PLAYER_NOT_INSTALLED -> _state.dispatchValue(
                _state.value?.copy(spotifyInstallButtonVisibility = View.VISIBLE)
            )
            CheckSetup.Status.PLAYER_NOT_CONNECTED -> _state.dispatchValue(
                _state.value?.copy(
                    spotifyInstallButtonVisibility = View.GONE,
                    spotifyInstallDoneVisibility = View.VISIBLE,
                    spotifyLoginButtonVisibility = View.VISIBLE
                )
            )
            CheckSetup.Status.READY -> _state.dispatchValue(
                _state.value?.copy(
                    spotifyInstallButtonVisibility = View.GONE,
                    spotifyInstallDoneVisibility = View.VISIBLE,
                    spotifyLoginButtonVisibility = View.GONE,
                    spotifyLoginDoneVisibility = View.VISIBLE,
                    startRunningButtonVisibility = View.VISIBLE
                )
            )
        }
    }

    fun onInstallPlayer() {
        installJob?.cancel()
        installJob = viewModelScope.launch {
            installPlayer.execute()
            check()
            installJob = null
        }
    }

    fun onConnectPlayer() {
        viewModelScope.launch {
            connectPlayer.execute()
            check()
        }
    }

    data class State(
        val spotifyInstallButtonVisibility: Int = View.GONE,
        val spotifyInstallDoneVisibility: Int = View.GONE,
        val spotifyLoginButtonVisibility: Int = View.GONE,
        val spotifyLoginDoneVisibility: Int = View.GONE,
        val startRunningButtonVisibility: Int = View.GONE
    )

    sealed class Effect {
        object SetupCompleted : Effect()
    }
}
