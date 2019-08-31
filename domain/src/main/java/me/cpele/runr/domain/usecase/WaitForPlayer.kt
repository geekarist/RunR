package me.cpele.runr.domain.usecase

import kotlinx.coroutines.delay
import me.cpele.runr.domain.adapter.Player
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class WaitForPlayer(
    private val player: Player
) {
    suspend fun execute(timeoutSec: Int = 60) {

        val times: Int = timeoutSec / INTERVAL_SEC

        repeat(times) {
            delay(TimeUnit.SECONDS.toMillis(INTERVAL_SEC.toLong()))
            if (player.isConnected) return
        }

        throw TimeoutException("Player is not connected after waiting for $timeoutSec seconds")
    }

    companion object {
        private const val INTERVAL_SEC: Int = 1
    }
}
