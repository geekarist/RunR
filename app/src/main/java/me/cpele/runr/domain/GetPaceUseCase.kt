package me.cpele.runr.domain

class GetPaceUseCase(private val paceRepository: PaceRepository) {

    fun execute(): Response {
        val pace: Int = paceRepository.get()
        return Response(pace, pace.toString())
    }

    data class Response(val pace: Int, val paceStr: String)
}
