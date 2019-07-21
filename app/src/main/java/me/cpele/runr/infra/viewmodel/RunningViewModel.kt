package me.cpele.runr.infra.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.cpele.runr.domain.usecase.DecreasePaceUseCase
import me.cpele.runr.domain.usecase.GetPaceUseCase
import me.cpele.runr.domain.usecase.IncreasePaceUseCase

class RunningViewModel(
    private val increasePaceUseCase: IncreasePaceUseCase,
    private val getPaceUseCase: GetPaceUseCase,
    private val decreasePaceUseCase: DecreasePaceUseCase
) : ViewModel() {

    private val _state = MutableLiveData<State>().apply {
        value =
            State(coverUriStr = "http://slowwly.robertomurray.co.uk/delay/10000/url/https://img.discogs.com/4XvJZQu82IRMe_AqSaKHNiaHmEw=/fit-in/300x300/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-2105567-1364683794-9014.jpeg.jpg")
    }
    val state: LiveData<State> = _state

    data class State(val stepsPerMinText: String = "", val coverUriStr: String?)

    init {
        viewModelScope.launch {
            val response = getPaceUseCase.execute()
            withContext(Dispatchers.Main) {
                _state.value = _state.value?.copy(stepsPerMinText = response.paceStr)
            }
        }
    }

    fun onIncreasePace() = viewModelScope.launch {
        val response = increasePaceUseCase.execute()
        withContext(Dispatchers.Main) {
            _state.value = _state.value?.copy(stepsPerMinText = response.newPaceStr)
        }
    }

    fun onDecreasePace() = viewModelScope.launch {
        val response = decreasePaceUseCase.execute()
        withContext(Dispatchers.Main) {
            _state.value = _state.value?.copy(stepsPerMinText = response.newPaceStr)
        }
    }
}
