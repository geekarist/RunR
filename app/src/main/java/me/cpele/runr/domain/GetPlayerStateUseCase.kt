package me.cpele.runr.domain

import kotlinx.coroutines.channels.Channel
import me.cpele.runr.domain.bo.PlayerStateBo
import me.cpele.runr.domain.iface.Player

@Suppress("EXPERIMENTAL_API_USAGE")
class GetPlayerStateUseCase(
    private val player: Player
) {
    suspend fun execute(): Channel<PlayerStateBo> {
        val channel = Channel<PlayerStateBo>()
        player.ensureReady()
        val subscription = player.subscribeToState()
        subscription.setEventCallback { isPaused ->
            channel.offer(PlayerStateBo(!isPaused))
        }
        channel.invokeOnClose { subscription.cancel() }
        return channel
    }
}
