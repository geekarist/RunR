package me.cpele.runr.infra.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.cpele.runr.domain.adapter.Player
import me.cpele.runr.infra.Consumable

class MainViewModel(private val player: Player) : ViewModel() {

    // TODO: Rename to effect
    private val _event = MutableLiveData<Consumable<Event>>()
    val event: LiveData<Consumable<Event>> get() = _event

    init {
        viewModelScope.launch {
            try {
                player.connect()
            } catch (e: Exception) {
                _event.value = Consumable(Event.Snack("Error connecting to player"))
            }
        }
    }

    override fun onCleared() {
        player.disconnect()
        super.onCleared()
    }

    // TODO: Rename to Effect
    sealed class Event {
        data class Snack(val message: String) : Event()
    }
}
