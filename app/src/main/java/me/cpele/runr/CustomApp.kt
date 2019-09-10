package me.cpele.runr

import android.app.Application
import com.google.gson.Gson
import me.cpele.runr.domain.usecase.*
import me.cpele.runr.infra.model.SpotifyAuthorizationAsync
import me.cpele.runr.infra.model.SpotifyPlayer
import me.cpele.runr.infra.model.data.PrefAuthResponseRepository
import me.cpele.runr.infra.model.data.SharedPrefsPaceRepository
import me.cpele.runr.infra.model.data.SpotifyPlaylistRepository
import me.cpele.runr.infra.model.data.SpotifyTrackRepository
import me.cpele.runr.infra.model.network.SpotifyService
import me.cpele.runr.infra.viewmodel.CheckSetupViewModel
import me.cpele.runr.infra.viewmodel.MainViewModel
import me.cpele.runr.infra.viewmodel.RunningViewModel
import me.cpele.runr.infra.viewmodel.ViewModelFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomApp : Application() {

    ///////////////////////////////////////////////////////////////////////////
    // Inject infrastructure
    ///////////////////////////////////////////////////////////////////////////

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
    private val tokenProvider = GetAuth(
        SpotifyAuthorizationAsync(this),
        PrefAuthResponseRepository(Gson(), this)
    )
    private val trackRepository =
        SpotifyTrackRepository(tokenProvider, spotifyService)
    private val playlistRepository =
        SpotifyPlaylistRepository(spotifyService, tokenProvider)
    private val paceRepository = SharedPrefsPaceRepository(this)
    private val player = SpotifyPlayer(this, tokenProvider)

    ///////////////////////////////////////////////////////////////////////////
    // Inject use cases
    ///////////////////////////////////////////////////////////////////////////

    private val startRunUseCase =
        StartRun(trackRepository, playlistRepository, player)
    private val increasePaceUseCase =
        ChangePace(paceRepository, startRunUseCase)
    private val getPaceUseCase = GetPace(paceRepository)
    private val emitPlayerStateUseCase = ObservePlayerState(player)
    private val waitForPlayer = WaitForPlayer(player)
    private val checkSetup = CheckSetup(player)
    private val installPlayer = InstallPlayer(player)

    ///////////////////////////////////////////////////////////////////////////
    // Inject ViewModels
    ///////////////////////////////////////////////////////////////////////////

    val mainViewModelFactory = ViewModelFactory { MainViewModel(player) }
    val runningViewModelFactory = ViewModelFactory {
        RunningViewModel(
            changePace = increasePaceUseCase,
            getPace = getPaceUseCase,
            observePlayerState = emitPlayerStateUseCase,
            waitForPlayer = waitForPlayer,
            startRun = startRunUseCase
        )
    }
    val checkSetupViewModelFactory =
        ViewModelFactory { CheckSetupViewModel(checkSetup, installPlayer) }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CustomApp
    }
}
