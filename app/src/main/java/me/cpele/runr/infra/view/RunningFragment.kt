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

        viewModel.state.observe(this, Observer { state: RunningViewModel.State? ->
            running_spm_value.text = state?.stepsPerMinText
            state?.coverUriStr?.let { coverUrl ->
                loadCover(coverUrl, running_track_cover) {
                    running_track_cover_progress.visibility = View.GONE
                }
            }
            state?.apply {
                running_track_cover.visibility = coverVisibility
                running_no_track.visibility = noTrackVisibility
            }
        })

        running_spm_increase.setOnClickListener { viewModel.onIncreasePace() }
        running_spm_decrease.setOnClickListener { viewModel.onDecreasePace() }
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
