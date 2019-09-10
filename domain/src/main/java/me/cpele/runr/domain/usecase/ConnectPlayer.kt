package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.adapter.Player

class ConnectPlayer(private val player: Player, private val waitForPlayer: WaitForPlayer) {
    suspend fun execute() {
        player.connect()
        waitForPlayer.execute()
    }
}
