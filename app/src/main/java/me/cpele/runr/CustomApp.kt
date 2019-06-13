package me.cpele.runr

import android.app.Application

class CustomApp : Application() {

    private val tokenProvider = TokenProvider(this)
    private val trackRepository = TrackRepository(tokenProvider)
    private val playlistRepository = PlaylistRepository()
    private val player = Player()

    val mainViewModelFactory =
        ViewModelFactory {
            MainViewModel(
                trackRepository,
                playlistRepository,
                player
            )
        }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CustomApp
    }
}
