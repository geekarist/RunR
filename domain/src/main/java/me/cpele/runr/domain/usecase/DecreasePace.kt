package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.iface.PaceRepository

class DecreasePace(
    private val paceRepository: PaceRepository,
    private val startRun: StartRun
) {

    suspend fun execute(): Response {
        val currentPace = paceRepository.get()
        val newPace = currentPace - PACE_DECREMENT
        paceRepository.set(newPace)
        startRun.execute(newPace)
        return Response(
            newPace.toString(),
            newPace
        )
    }

    data class Response(val newPaceStr: String, val newPace: Int)

    companion object {
        private const val PACE_DECREMENT = 5
    }
}
