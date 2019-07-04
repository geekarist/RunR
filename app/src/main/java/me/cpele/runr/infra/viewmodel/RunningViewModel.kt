package me.cpele.runr.infra.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RunningViewModel : ViewModel() {

    val state: LiveData<State> =
        MutableLiveData<State>().apply { value = State("140") }

    data class State(val stepsPerMinText: CharSequence)
}
