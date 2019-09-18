package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.adapter.PaceRepository
import me.cpele.runr.domain.adapter.Player


// TODO: Implement UseCase<RequestT, ResponseT> interface
class ChangePace(
    private val paceRepository: PaceRepository,
    private val startRun: StartRun,
    private val player: Player
) {

    suspend fun execute(direction: Direction): Response {
        val currentPace = paceRepository.get()
        val newPace = currentPace + direction.step
        paceRepository.set(newPace)
        player.pause()
        return when (startRun.execute(newPace)) {
            is StartRun.Response.Success -> Response.Success(newPace.toString())
            is StartRun.Response.NoTrackFound -> Response.NoTrackFound(newPace.toString())
        }
    }

    sealed class Response(val newPaceStr: String) {
        class Success(newPaceStr: String) : Response(newPaceStr)
        class NoTrackFound(newPaceStr: String) : Response(newPaceStr)
    }

    companion object {
        private const val PACE_INCREMENT = 5
    }

    enum class Direction(val step: Int) {
        Increase(PACE_INCREMENT),
        Decrease(-PACE_INCREMENT)
    }
}
