package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.adapter.Player
import me.cpele.runr.domain.adapter.SetupStatusRepository
import java.util.concurrent.TimeoutException

class CheckSetup(
    private val player: Player,
    private val waitForPlayer: WaitForPlayer,
    private val setupStatusRepository: SetupStatusRepository
) {

    suspend fun execute(): Response {

        if (setupStatusRepository.value == SetupStatusRepository.Status.DONE) {
            return Response(Status.CHECK_ALREADY_DONE)
        }

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

        setupStatusRepository.value = SetupStatusRepository.Status.DONE

        return Response(Status.READY)
    }

    data class Response(val status: Status)

    enum class Status {
        PLAYER_NOT_INSTALLED,
        PLAYER_NOT_CONNECTED,
        CHECK_ALREADY_DONE,
        READY
    }
}
