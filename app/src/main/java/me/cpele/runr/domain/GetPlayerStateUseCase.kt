package me.cpele.runr.domain

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import me.cpele.runr.domain.bo.PlayerStateBo

class GetPlayerStateUseCase {
    suspend fun execute(): Channel<PlayerStateBo> {
        val channel = Channel<PlayerStateBo>()
        delay(2000)
        channel.send(PlayerStateBo(true))
        delay(2000)
        channel.send(PlayerStateBo(false))
        delay(2000)
        channel.send(PlayerStateBo(true))
        delay(2000)
        return channel
    }

}
