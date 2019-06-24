package me.cpele.runr

import android.app.Application
import me.cpele.runr.model.Player
import me.cpele.runr.model.SpotifyAppRemoteProvider
import me.cpele.runr.model.TokenProvider
import me.cpele.runr.model.data.PlaylistRepository
import me.cpele.runr.model.data.TrackRepository
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
        TrackRepository(tokenProvider, spotifyService)
    private val playlistRepository =
        PlaylistRepository(spotifyService, tokenProvider)
    private val spotifyAppRemoteProvider = SpotifyAppRemoteProvider(this)
    private val player = Player(spotifyAppRemoteProvider)

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
