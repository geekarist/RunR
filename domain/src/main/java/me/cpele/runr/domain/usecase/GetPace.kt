package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.adapter.PaceRepository

class GetPace(private val paceRepository: PaceRepository) {

    fun execute(): Response {
        val pace: Int = paceRepository.get()
        return Response(pace, pace.toString())
    }

    data class Response(val pace: Int, val paceStr: String)
}
