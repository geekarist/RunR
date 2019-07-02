package me.cpele.runr.infra.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.start_run_fragment.*
import me.cpele.runr.CustomApp
import me.cpele.runr.R
import me.cpele.runr.infra.viewmodel.StartRunViewModel

class StartRunFragment : Fragment() {

    companion object {
        fun newInstance() = StartRunFragment()
    }

    private lateinit var viewModel: StartRunViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.start_run_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            CustomApp.instance.mainViewModelFactory
        ).get(StartRunViewModel::class.java)
        main_start_run_button.setOnClickListener {
            viewModel.onStartRunClicked()
            findNavController().navigate(R.id.action_startRunFragment_to_runningFragment)
        }
    }
}
