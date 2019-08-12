package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.entities.PlayerState
import me.cpele.runr.domain.iface.Player

class GetPlayerState(
    private val player: Player
) {
    suspend fun execute(): PlayerState = player.state().let {
        val isPlaying = it?.isPaused == false
        PlayerState(isPlaying, it?.coverUrl)
    }
}
