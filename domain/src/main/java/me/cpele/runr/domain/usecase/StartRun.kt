package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.iface.Player
import me.cpele.runr.domain.iface.PlaylistRepository
import me.cpele.runr.domain.iface.TrackRepository

class StartRun(
    private val trackRepository: TrackRepository,
    private val playlistRepository: PlaylistRepository,
    private val player: Player
) {
    suspend fun execute(pace: Int): Response {
        val tracks = trackRepository.findByPace(pace)
        return if (tracks.isNotEmpty()) {
            val playlist = playlistRepository.create(tracks)
            player.play(playlist)
            Response.Success
        } else {
            Response.NoTrackFound
        }
    }

    sealed class Response {
        object Success : Response()
        object NoTrackFound : Response()
    }
}
