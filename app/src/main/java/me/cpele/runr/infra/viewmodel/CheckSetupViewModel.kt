package me.cpele.runr.infra.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.cpele.runr.infra.Event

class CheckSetupViewModel : ViewModel() {

    private val _effect = MutableLiveData<Event<Effect>>()
    val effect: LiveData<Event<Effect>> get() = _effect

    sealed class Effect {
        object SetupCompleted : Effect()
    }
}
