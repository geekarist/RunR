package me.cpele.runr.ui.main

import android.app.Application

class CustomApp : Application() {

    private val trackRepository = TrackRepository()
    private val playlistRepository = PlaylistRepository()
    private val player = Player()

    val mainViewModelFactory =
        ViewModelFactory { MainViewModel(trackRepository, playlistRepository, player) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CustomApp
    }
}
