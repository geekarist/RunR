package me.cpele.runr.infra.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RunningViewModel : ViewModel() {

    val viewState: LiveData<ViewState> =
        MutableLiveData<ViewState>().apply { value = ViewState("140") }

    data class ViewState(val stepsPerMinText: CharSequence)
}
