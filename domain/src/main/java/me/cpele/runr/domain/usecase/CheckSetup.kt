package me.cpele.runr.domain.usecase

class CheckSetup {
    fun execute(): Response {
        return Response(Status.READY)
    }

    data class Response(val status: Status)

    enum class Status {
        PLAYER_NOT_INSTALLED,
        PLAYER_NOT_LOGGED_IN,
        READY
    }
}
