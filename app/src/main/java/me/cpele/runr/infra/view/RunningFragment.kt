package me.cpele.runr.infra.view


import android.graphics.drawable.Drawable
import android.os.Bundle
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

        running_spm_increase.setOnClickListener { viewModel.onIncreasePace() }
        running_spm_decrease.setOnClickListener { viewModel.onDecreasePace() }

        viewModel.onOrientationChanged(resources.configuration?.orientation)
    }

    private fun render(state: RunningViewModel.State) {
        running_spm_value.text = state.stepsPerMinText
        loadCover(state.coverUriStr, running_track_cover) {
            running_track_cover_progress.visibility = View.GONE
        }
        running_track_cover.visibility = state.coverVisibility
        running_track_cover.scaleType = state.scaleType
        running_no_track.visibility = state.noTrackVisibility
    }

    private fun loadCover(url: String?, target: ImageView, onLoadFinished: () -> Unit) {
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
                    onLoadFinished()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    onLoadFinished()
                    return false
                }
            })
            .into(target)
    }
}
