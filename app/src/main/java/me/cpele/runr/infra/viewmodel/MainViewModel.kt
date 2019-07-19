package me.cpele.runr.infra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.cpele.runr.domain.iface.Player

class MainViewModel(private val player: Player) : ViewModel() {

    init {
        viewModelScope.launch {
            player.connect()
        }
    }

    override fun onCleared() {
        player.disconnect()
        super.onCleared()
    }
}
