package me.cpele.runr.ui.main

import androidx.lifecycle.ViewModel

class MainViewModel(
    private val trackRepository: TrackRepository,
    private val playlistRepository: PlaylistRepository,
    private val player: Player
) : ViewModel() {

    fun onStartRunClicked() {
        val tracks = trackRepository.findByPace(100)
        val playlist = PlaylistBo(tracks)
        playlistRepository.insert(playlist)
        player.play(playlist)
    }
}