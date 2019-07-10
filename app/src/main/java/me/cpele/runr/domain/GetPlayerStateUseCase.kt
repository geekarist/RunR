package me.cpele.runr.domain

import kotlinx.coroutines.channels.Channel
import me.cpele.runr.domain.bo.PlayerStateBo
import me.cpele.runr.domain.iface.Player

@Suppress("EXPERIMENTAL_API_USAGE")
class GetPlayerStateUseCase(
    private val player: Player
) {
    suspend fun execute(): Channel<PlayerStateBo> {
        return Channel<PlayerStateBo>().apply {
            val subscription = player.subscribeToState()
            subscription.setEventCallback { isPaused ->
                offer(PlayerStateBo(!isPaused))
            }
            invokeOnClose { subscription.cancel() }
        }
    }
}
