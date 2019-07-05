package me.cpele.runr.infra.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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

        viewModel.state.observe(this, Observer {
            running_spm_value.text = it?.stepsPerMinText
        })

        running_spm_increase.setOnClickListener { viewModel.onIncreasePace() }
        running_spm_decrease.setOnClickListener { viewModel.onDecreasePace() }
    }
}
