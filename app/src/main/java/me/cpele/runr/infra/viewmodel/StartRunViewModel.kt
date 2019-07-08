package me.cpele.runr.infra.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.cpele.runr.domain.GetPlayerStateUseCase
import me.cpele.runr.domain.usecase.StartRunUseCase

class StartRunViewModel(
    private val startRunUseCase: StartRunUseCase,
    private val getPlayerStateUseCase: GetPlayerStateUseCase
) : ViewModel() {

    private val _state = MutableLiveData<State>().apply { value = State(false) }
    val state: LiveData<State> = _state

    init {
        viewModelScope.launch {
            for (playerState in getPlayerStateUseCase.execute()) {
                _state.value = _state.value?.copy(continueRunEnabled = playerState.isPlaying)
            }
        }
    }

    fun onStartRunClicked() = viewModelScope.launch {
        startRunUseCase.execute(100)
    }

    data class State(val continueRunEnabled: Boolean)
}