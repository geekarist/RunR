package me.cpele.runr.domain

class StartRunUseCase(
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
