package me.cpele.runr

import android.app.Application
import me.cpele.runr.domain.StartRunUseCase
import me.cpele.runr.model.SpotifyAppRemoteProvider
import me.cpele.runr.model.SpotifyPlayer
import me.cpele.runr.model.TokenProvider
import me.cpele.runr.model.data.SpotifyPlaylistRepository
import me.cpele.runr.model.data.SpotifyTrackRepository
import me.cpele.runr.model.network.SpotifyService
import me.cpele.runr.viewmodel.MainViewModel
import me.cpele.runr.viewmodel.ViewModelFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomApp : Application() {

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        ).build()
    private val spotifyService: SpotifyService =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.spotify.com")
            .client(httpClient)
            .build()
            .create(SpotifyService::class.java)
    private val tokenProvider = TokenProvider(this)
    private val trackRepository =
        SpotifyTrackRepository(tokenProvider, spotifyService)
    private val playlistRepository =
        SpotifyPlaylistRepository(spotifyService, tokenProvider)
    private val spotifyAppRemoteProvider = SpotifyAppRemoteProvider(this)
    private val player = SpotifyPlayer(spotifyAppRemoteProvider)
    private val startRunUseCase = StartRunUseCase(trackRepository, playlistRepository, player)

    val mainViewModelFactory = ViewModelFactory { MainViewModel(startRunUseCase) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CustomApp
    }
}
