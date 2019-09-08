package me.cpele.runr.domain.usecase

class CheckSetup {
    fun execute(): Response {
        return Response(true)
    }

    data class Response(val isCompleted: Boolean)
}
