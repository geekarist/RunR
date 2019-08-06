package me.cpele.runr

import android.app.Application
import com.google.gson.Gson
import me.cpele.runr.domain.TokenProvider
import me.cpele.runr.domain.usecase.*
import me.cpele.runr.infra.model.SpotifyAuthorizationAsync
import me.cpele.runr.infra.model.SpotifyPlayer
import me.cpele.runr.infra.model.data.PrefAuthResponseRepository
import me.cpele.runr.infra.model.data.SharedPrefsPaceRepository
import me.cpele.runr.infra.model.data.SpotifyPlaylistRepository
import me.cpele.runr.infra.model.data.SpotifyTrackRepository
import me.cpele.runr.infra.model.network.SpotifyService
import me.cpele.runr.infra.viewmodel.MainViewModel
import me.cpele.runr.infra.viewmodel.RunningViewModel
import me.cpele.runr.infra.viewmodel.ViewModelFactory
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
    private val tokenProvider = TokenProvider(
        SpotifyAuthorizationAsync(this),
        PrefAuthResponseRepository(Gson(), this)
    )
    private val trackRepository =
        SpotifyTrackRepository(tokenProvider, spotifyService)
    private val playlistRepository =
        SpotifyPlaylistRepository(spotifyService, tokenProvider)
    private val paceRepository = SharedPrefsPaceRepository(this)
    val player = SpotifyPlayer(this, tokenProvider)
    private val startRunUseCase =
        StartRun(trackRepository, playlistRepository, player)
    private val getPlayerStateUseCase = GetPlayerState(player)
    private val increasePaceUseCase =
        IncreasePace(paceRepository, startRunUseCase)
    private val decreasePaceUseCase =
        DecreasePace(paceRepository, startRunUseCase)
    private val getPaceUseCase = GetPace(paceRepository)
    private val emitPlayerStateUseCase = ObservePlayerState(player)

    val mainViewModelFactory = ViewModelFactory { MainViewModel(player) }
    val runningViewModelFactory = ViewModelFactory {
        RunningViewModel(
            increasePace = increasePaceUseCase,
            getPace = getPaceUseCase,
            decreasePace = decreasePaceUseCase,
            observePlayerState = emitPlayerStateUseCase
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
