package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.iface.PaceRepository


class IncreasePace(
    private val paceRepository: PaceRepository,
    private val startRun: StartRun,
    private val getGetPlayerState: GetPlayerState
) {

    suspend fun execute(): Response {
        val currentPace = paceRepository.get()
        val newPace = currentPace + PACE_INCREMENT
        paceRepository.set(newPace)
        startRun.execute(newPace)
        return Response(
            newPace.toString(),
            newPace
        )
    }

    class Response(
        val newPaceStr: String,
        val newPace: Int
    )

    companion object {
        private const val PACE_INCREMENT = 5
    }
}
