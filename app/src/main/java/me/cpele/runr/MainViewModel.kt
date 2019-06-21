package me.cpele.runr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

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