package me.cpele.runr.domain

import kotlinx.coroutines.channels.Channel
import me.cpele.runr.domain.bo.PlayerStateBo
import me.cpele.runr.infra.model.SpotifyAppRemoteProvider

@Suppress("EXPERIMENTAL_API_USAGE")
class GetPlayerStateUseCase(
    private val appRemoteProvider: SpotifyAppRemoteProvider
) {
    suspend fun execute(): Channel<PlayerStateBo> {
        return Channel<PlayerStateBo>().apply {
            val appRemote = appRemoteProvider.get()
            val subscription = appRemote.playerApi.subscribeToPlayerState()
            subscription.setEventCallback {
                offer(PlayerStateBo(!it.isPaused))
            }
            invokeOnClose { subscription.cancel() }
        }
    }
}
