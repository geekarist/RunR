package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.bo.PlayerStateBo
import me.cpele.runr.domain.iface.PaceRepository


class IncreasePaceUseCase(
    private val paceRepository: PaceRepository,
    private val startRunUseCase: StartRunUseCase,
    private val getPlayerStateUseCase: PlayerStateUseCase
) {

    suspend fun execute(): Response {
        val currentPace = paceRepository.get()
        val newPace = currentPace + PACE_INCREMENT
        paceRepository.set(newPace)
        startRunUseCase.execute(newPace)
        val playerState: PlayerStateBo = getPlayerStateUseCase.execute()
        return Response(
            newPace.toString(),
            newPace,
            playerState.coverUrl
        )
    }

    class Response(
        val newPaceStr: String,
        val newPace: Int,
        val coverUrl: String?
    )

    companion object {
        private const val PACE_INCREMENT = 5
    }
}
