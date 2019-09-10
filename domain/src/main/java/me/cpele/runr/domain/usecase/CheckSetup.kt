package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.adapter.Player
import java.util.concurrent.TimeoutException

class CheckSetup(private val player: Player, private val waitForPlayer: WaitForPlayer) {

    suspend fun execute(): Response {

        if (!player.isInstalled) {
            return Response(Status.PLAYER_NOT_INSTALLED)
        }

        val isTimeout = try {
            waitForPlayer.execute(2)
            false
        } catch (e: TimeoutException) {
            true
        }

        if (isTimeout || !player.isConnected) {
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
