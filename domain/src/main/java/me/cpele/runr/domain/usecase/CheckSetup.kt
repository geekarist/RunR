package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.adapter.Player

class CheckSetup(private val player: Player) {

    fun execute(): Response {

        if (!player.isInstalled) {
            return Response(Status.PLAYER_NOT_INSTALLED)
        }

        if (!player.isConnected) {
            return Response(Status.PLAYER_NOT_CONNECTED)
        }

        return Response(Status.READY)
    }

    data class Response(val status: Status)

    enum class Status {
        PLAYER_NOT_INSTALLED,
        PLAYER_NOT_CONNECTED,
        READY
    }
}
