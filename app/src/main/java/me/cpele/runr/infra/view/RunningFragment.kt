package me.cpele.runr.infra.view


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_running.*
import me.cpele.runr.CustomApp
import me.cpele.runr.R
import me.cpele.runr.infra.Event
import me.cpele.runr.infra.viewmodel.RunningViewModel

class RunningFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders
            .of(this, CustomApp.instance.runningViewModelFactory)
            .get(RunningViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_running, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(this, Observer { render(it) })
        viewModel.effect.observe(this, Observer { render(it) })

        running_spm_increase.setOnClickListener { viewModel.onIncreasePace() }
        running_spm_decrease.setOnClickListener { viewModel.onDecreasePace() }
    }

    private fun render(event: Event<RunningViewModel.Effect>?) {
        when (val effect = event?.value) {
            is RunningViewModel.Effect.Message ->
                Log.i(javaClass.simpleName, effect.message)
        }
    }

    private fun render(state: RunningViewModel.State) {
        running_spm_value.text = state.stepsPerMinText
        running_progress.visibility = state.progressVisibility
        loadCover(state.coverUriStr, running_track_cover)
        running_track_cover.visibility = state.coverVisibility
        running_track_cover.scaleType = state.scaleType
        running_no_track.visibility = state.noTrackVisibility
        running_spm_increase.isEnabled = state.isChangePaceEnabled
        running_spm_decrease.isEnabled = state.isChangePaceEnabled
        running_track_title.text = state.trackTitle
        running_track_artist.text = state.trackArtist
    }

    private fun loadCover(url: String?, target: ImageView) {
        if (url == null) {
            Glide.with(this).clear(target)
            return
        }

        Glide.with(this)
            .load(url)
            .timeout(10000)
            .addListener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(target)
    }
}
