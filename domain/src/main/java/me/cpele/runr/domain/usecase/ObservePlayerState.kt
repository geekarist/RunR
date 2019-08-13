package me.cpele.runr.domain.usecase

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.map
import kotlinx.coroutines.coroutineScope
import me.cpele.runr.domain.adapter.Player

// TODO: Implement UseCase<RequestT, ReceiveChannel<ResponseT>> interface
class ObservePlayerState(private val player: Player) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    suspend fun execute(): ReceiveChannel<Response> = coroutineScope {
        player.observeStateForever().map {
            Response(!it.isPaused, it.coverUrl)
        }
    }

    data class Response(val isPlaying: Boolean, val coverUrl: String)
}
