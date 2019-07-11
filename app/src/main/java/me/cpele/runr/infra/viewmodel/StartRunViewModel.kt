package me.cpele.runr.infra.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.cpele.runr.domain.GetPlayerStateUseCase
import me.cpele.runr.domain.bo.PlayerStateBo
import me.cpele.runr.domain.usecase.StartRunUseCase

class StartRunViewModel(
    private val startRunUseCase: StartRunUseCase,
    private val getPlayerStateUseCase: GetPlayerStateUseCase
) : ViewModel() {

    private var playerStates: Channel<PlayerStateBo>? = null

    private val _state = MutableLiveData<State>().apply { value = State(false) }
    val state: LiveData<State> = _state

    init {
        viewModelScope.launch {
            playerStates = getPlayerStateUseCase.execute()
            playerStates?.let { states ->
                for (playState in states) {
                    withContext(Dispatchers.Main) {
                        _state.value = _state.value?.copy(continueRunEnabled = playState.isPlaying)
                    }
                }
            }
        }
    }

    fun onStartRunClicked() = viewModelScope.launch {
        startRunUseCase.execute(100)
    }

    override fun onCleared() {
        playerStates?.close()
        super.onCleared()
    }

    data class State(val continueRunEnabled: Boolean)
}