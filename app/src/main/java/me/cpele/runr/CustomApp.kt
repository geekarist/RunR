package me.cpele.runr

import android.app.Application
import com.google.gson.Gson
import me.cpele.runr.domain.GetPlayerStateUseCase
import me.cpele.runr.domain.TokenProvider
import me.cpele.runr.domain.usecase.DecreasePaceUseCase
import me.cpele.runr.domain.usecase.GetPaceUseCase
import me.cpele.runr.domain.usecase.IncreasePaceUseCase
import me.cpele.runr.domain.usecase.StartRunUseCase
import me.cpele.runr.infra.model.SpotifyAppRemoteProvider
import me.cpele.runr.infra.model.SpotifyAuthorizationAsync
import me.cpele.runr.infra.model.SpotifyPlayer
import me.cpele.runr.infra.model.data.PrefAuthResponseRepository
import me.cpele.runr.infra.model.data.SharedPrefsPaceRepository
import me.cpele.runr.infra.model.data.SpotifyPlaylistRepository
import me.cpele.runr.infra.model.data.SpotifyTrackRepository
import me.cpele.runr.infra.model.network.SpotifyService
import me.cpele.runr.infra.viewmodel.RunningViewModel
import me.cpele.runr.infra.viewmodel.StartRunViewModel
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
    private val spotifyAppRemoteProvider = SpotifyAppRemoteProvider(this)
    private val player = SpotifyPlayer(spotifyAppRemoteProvider)
    private val startRunUseCase =
        StartRunUseCase(trackRepository, playlistRepository, player)
    private val increasePaceUseCase =
        IncreasePaceUseCase(paceRepository, startRunUseCase)
    private val decreasePaceUseCase =
        DecreasePaceUseCase(paceRepository, startRunUseCase)
    private val getPaceUseCase = GetPaceUseCase(paceRepository)
    private val getPlayerStateUseCase = GetPlayerStateUseCase(player)

    val mainViewModelFactory = ViewModelFactory {
        StartRunViewModel(
            startRunUseCase,
            getPlayerStateUseCase
        )
    }
    val runningViewModelFactory = ViewModelFactory {
        RunningViewModel(
            increasePaceUseCase = increasePaceUseCase,
            getPaceUseCase = getPaceUseCase,
            decreasePaceUseCase = decreasePaceUseCase
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
