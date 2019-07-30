package me.cpele.runr.domain

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.map
import kotlinx.coroutines.coroutineScope
import me.cpele.runr.domain.iface.Player

class EmitPlayerStateUseCase(private val player: Player) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    suspend fun execute(): ReceiveChannel<Response> = coroutineScope {
        player.observeStateForever().map {
            Response(!it.isPaused, it.coverUrl)
        }
    }

    data class Response(val isPlaying: Boolean, val coverUrl: String)
}
