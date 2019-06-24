package me.cpele.runr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.cpele.runr.model.Player
import me.cpele.runr.model.data.PlaylistRepository
import me.cpele.runr.model.data.TrackRepository

class MainViewModel(
    private val trackRepository: TrackRepository,
    private val playlistRepository: PlaylistRepository,
    private val player: Player
) : ViewModel() {

    fun onStartRunClicked() = viewModelScope.launch {
        val tracks = trackRepository.findByPace(100)
        val playlist = playlistRepository.create(tracks)
        player.play(playlist)
    }
}