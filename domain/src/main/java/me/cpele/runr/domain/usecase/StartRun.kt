package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.iface.Player
import me.cpele.runr.domain.iface.PlaylistRepository
import me.cpele.runr.domain.iface.TrackRepository

class StartRun(
    private val trackRepository: TrackRepository,
    private val playlistRepository: PlaylistRepository,
    private val player: Player
) {
    suspend fun execute(pace: Int) {
        val tracks = trackRepository.findByPace(pace)
        val playlist = playlistRepository.create(tracks)
        player.play(playlist)
    }
}
