package me.cpele.runr.infra.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.cpele.runr.domain.usecase.CheckSetup
import me.cpele.runr.infra.Event

class CheckSetupViewModel(private val checkSetup: CheckSetup) : ViewModel() {

    private val _effect = MutableLiveData<Event<Effect>>()
    val effect: LiveData<Event<Effect>> get() = _effect

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> get() = _state

    init {
        viewModelScope.launch {
            _state.dispatchValue(State())
            val response = checkSetup.execute()
            when (response.status) {
                CheckSetup.Status.PLAYER_NOT_INSTALLED -> _state.dispatchValue(
                    _state.value?.copy(spotifyInstallButtonVisibility = View.VISIBLE)
                )
                CheckSetup.Status.PLAYER_NOT_LOGGED_IN -> _state.dispatchValue(
                    _state.value?.copy(
                        spotifyInstallButtonVisibility = View.GONE,
                        spotifyLoginButtonVisibility = View.VISIBLE
                    )
                )
                CheckSetup.Status.READY -> _state.dispatchValue(
                    _state.value?.copy(
                        spotifyInstallButtonVisibility = View.GONE,
                        spotifyLoginButtonVisibility = View.GONE,
                        startRunningButtonVisibility = View.VISIBLE
                    )
                )
            }
        }
    }

    data class State(
        val spotifyInstallButtonVisibility: Int = View.GONE,
        val spotifyInstallDoneVisibility: Int = View.GONE,
        val spotifyLoginButtonVisibility: Int = View.GONE,
        val spotifyLoginDoneVisibility: Int = View.GONE,
        val startRunningButtonVisibility: Int = View.GONE,
        val startRunningDoneVisibility: Int = View.GONE
    )

    sealed class Effect {
        object SetupCompleted : Effect()
    }
}
