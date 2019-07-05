package me.cpele.runr.domain

class DecreasePaceUseCase(
    private val paceRepository: PaceRepository,
    private val startRunUseCase: StartRunUseCase
) {

    suspend fun execute(): Response {
        val currentPace = paceRepository.get()
        val newPace = currentPace - PACE_DECREMENT
        paceRepository.set(newPace)
        startRunUseCase.execute(newPace)
        return Response(newPace.toString(), newPace)
    }

    data class Response(val newPaceStr: String, val newPace: Int)

    companion object {
        private const val PACE_DECREMENT = 5
    }
}
