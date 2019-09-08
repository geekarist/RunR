package me.cpele.runr.infra.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_check_setup.*
import me.cpele.runr.CustomApp
import me.cpele.runr.R
import me.cpele.runr.infra.Event
import me.cpele.runr.infra.viewmodel.CheckSetupViewModel
import me.cpele.runr.infra.viewmodel.MainViewModel

class CheckSetupFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            CustomApp.instance.checkSetupViewModelFactory
        ).get(CheckSetupViewModel::class.java)
    }

    private val activityViewModel by lazy {
        activity?.let {
            ViewModelProviders.of(
                it,
                CustomApp.instance.mainViewModelFactory
            ).get(MainViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.effect.observe(
            this,
            Observer { event: Event<CheckSetupViewModel.Effect>? -> render(event) }
        )

        viewModel.state.observe(this, Observer { state -> render(state) })
    }

    private fun render(state: CheckSetupViewModel.State) {
        check_setup_install_button.visibility = state.spotifyInstallButtonVisibility
        check_setup_login_button.visibility = state.spotifyLoginButtonVisibility
        check_setup_start_button.visibility = state.startRunningButtonVisibility
    }

    private fun render(event: Event<CheckSetupViewModel.Effect>?) {
        when (event?.value) {
            is CheckSetupViewModel.Effect.SetupCompleted ->
                activityViewModel?.onSetupCompleted()
        }
    }
}
