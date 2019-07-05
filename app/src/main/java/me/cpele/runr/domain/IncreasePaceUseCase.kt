package me.cpele.runr.domain


class IncreasePaceUseCase(
    private val paceRepository: PaceRepository,
    private val startRunUseCase: StartRunUseCase
) {

    suspend fun execute(): Response {
        val currentPace = paceRepository.get()
        val newPace = currentPace + PACE_INCREMENT
        paceRepository.set(newPace)
        startRunUseCase.execute(newPace)
        return Response(newPace.toString(), newPace)
    }

    class Response(val newPaceStr: String, val newPace: Int)

    companion object {
        private const val PACE_INCREMENT = 5
    }
}
