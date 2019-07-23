package me.cpele.runr.domain.usecase

import me.cpele.runr.domain.bo.PlayerStateBo
import me.cpele.runr.domain.iface.Player

class PlayerStateUseCase(
    private val player: Player
) {
    fun execute(): PlayerStateBo = player.state.let {
        val isPlaying = it?.isPaused == false
        PlayerStateBo(isPlaying, it?.coverUrl)
    }
}
