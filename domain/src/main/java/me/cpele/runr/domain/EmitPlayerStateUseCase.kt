package me.cpele.runr.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay

class EmitPlayerStateUseCase {

    @Suppress("EXPERIMENTAL_API_USAGE")
    suspend fun execute(coroutineScope: CoroutineScope): ReceiveChannel<Response> =
        coroutineScope.produce {
            repeat(1000) {
                send(
                    Response(
                        true,
                        "https://img.discogs.com/4XvJZQu82IRMe_AqSaKHNiaHmEw=/fit-in/300x300/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-2105567-1364683794-9014.jpeg.jpg"
                    )
                )
                delay(2000)
                send(
                    Response(
                        true,
                        "https://img.discogs.com/UIdIAyisZnrKVMqLkF4i-f13_AM=/fit-in/600x600/filters:strip_icc():format(jpeg):mode_rgb():quality(90)/discogs-images/R-13779833-1562962146-3098.jpeg.jpg"
                    )
                )
                delay(2000)
            }
            close()
        }

    data class Response(val isPlaying: Boolean, val coverUrl: String)
}
