package me.cpele.runr.domain.usecase

import kotlinx.coroutines.delay
import me.cpele.runr.domain.adapter.Player
import java.util.concurrent.TimeUnit

class InstallPlayer(private val player: Player) {
    suspend fun execute() {
        player.install()
        val timeoutSec = 120
        repeat(timeoutSec) {
            delay(TimeUnit.SECONDS.toMillis(1))
            if (player.isInstalled) return
        }
    }
}
