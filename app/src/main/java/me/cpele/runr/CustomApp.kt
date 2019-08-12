package me.cpele.runr

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.annotation.DrawableRes
import com.google.gson.Gson
import me.cpele.runr.domain.TokenProvider
import me.cpele.runr.domain.usecase.ChangePace
import me.cpele.runr.domain.usecase.GetPace
import me.cpele.runr.domain.usecase.ObservePlayerState
import me.cpele.runr.domain.usecase.StartRun
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
    private val increasePaceUseCase =
        ChangePace(paceRepository, startRunUseCase)
    private val getPaceUseCase = GetPace(paceRepository)
    private val emitPlayerStateUseCase = ObservePlayerState(player)

    val mainViewModelFactory = ViewModelFactory { MainViewModel(player) }
    val runningViewModelFactory = ViewModelFactory {
        RunningViewModel(
            changePace = increasePaceUseCase,
            getPace = getPaceUseCase,
            observePlayerState = emitPlayerStateUseCase,
            application = this
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


fun Application.getUrl(@DrawableRes drawableRes: Int): String =
    Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(drawableRes))
        .appendPath(resources.getResourceTypeName(drawableRes))
        .appendPath(resources.getResourceEntryName(drawableRes))
        .build()
        .toString()
