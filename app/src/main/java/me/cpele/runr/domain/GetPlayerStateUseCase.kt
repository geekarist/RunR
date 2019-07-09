package me.cpele.runr.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import me.cpele.runr.domain.bo.PlayerStateBo

@Suppress("EXPERIMENTAL_API_USAGE")
class GetPlayerStateUseCase {
    fun execute(scope: CoroutineScope) = scope.produce {
        delay(2000)
        send(PlayerStateBo(true))
        delay(2000)
        send(PlayerStateBo(false))
        delay(2000)
        send(PlayerStateBo(true))
        delay(2000)
        close()
    }
}
