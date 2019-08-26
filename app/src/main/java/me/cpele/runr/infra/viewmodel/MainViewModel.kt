package me.cpele.runr.infra.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.cpele.runr.domain.adapter.Player
import me.cpele.runr.infra.Event

class MainViewModel(private val player: Player) : ViewModel() {

    private val _effect = MutableLiveData<Event<Effect>>()
    val effect: LiveData<Event<Effect>> get() = _effect

    init {
        viewModelScope.launch {
            try {
                player.connect()
            } catch (e: Exception) {
                Log.i(javaClass.simpleName, "Error connecting to player")
            }
        }
    }

    override fun onCleared() {
        player.disconnect()
        super.onCleared()
    }

    sealed class Effect {
        data class Message(val message: String) : Effect()
    }
}
