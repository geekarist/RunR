package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.bo.PlayerStateBo
import me.cpele.runr.domain.iface.Player

class GetPlayerState(
    private val player: Player
) {
    suspend fun execute(): PlayerStateBo = player.state().let {
        val isPlaying = it?.isPaused == false
        PlayerStateBo(isPlaying, it?.coverUrl)
    }
}
