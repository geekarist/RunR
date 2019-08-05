package me.cpele.runr.infra.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.cpele.runr.domain.usecase.DecreasePace
import me.cpele.runr.domain.usecase.GetPace
import me.cpele.runr.domain.usecase.IncreasePace
import me.cpele.runr.domain.usecase.ObservePlayerState

class RunningViewModel(
    private val increasePace: IncreasePace,
    private val getPace: GetPace,
    private val decreasePace: DecreasePace,
    val observePlayerState: ObservePlayerState
) : ViewModel() {

    private val _state = MutableLiveData<State>().apply {
        value =
            State(coverUriStr = "http://slowwly.robertomurray.co.uk/delay/5000/url/https://img.discogs.com/4XvJZQu82IRMe_AqSaKHNiaHmEw=/fit-in/300x300/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-2105567-1364683794-9014.jpeg.jpg")
    }
    val state: LiveData<State> = _state

    data class State(val stepsPerMinText: String = "", val coverUriStr: String?)

    init {
        viewModelScope.launch {
            val response = getPace.execute()
            val newValueWithPace = _state.value?.copy(stepsPerMinText = response.paceStr)
            if (_state.value != newValueWithPace) {
                withContext(Dispatchers.Main) { _state.value = newValueWithPace }
            }

            val channel = observePlayerState.execute()
            for (playerState in channel) {
                val newValueWithCover = _state.value?.copy(coverUriStr = playerState.coverUrl)
                if (_state.value != newValueWithCover) {
                    withContext(Dispatchers.Main) { _state.value = newValueWithCover }
                }
            }
        }
    }

    fun onIncreasePace() = viewModelScope.launch {
        val response = increasePace.execute()
        val newValue = _state.value?.copy(stepsPerMinText = response.newPaceStr)
        withContext(Dispatchers.Main) { _state.value = newValue }
    }

    fun onDecreasePace() = viewModelScope.launch {
        val response = decreasePace.execute()
        val newValue = _state.value?.copy(stepsPerMinText = response.newPaceStr)
        withContext(Dispatchers.Main) { _state.value = newValue }
    }
}
