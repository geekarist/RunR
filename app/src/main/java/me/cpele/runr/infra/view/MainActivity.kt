package me.cpele.runr.infra.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_activity.*
import me.cpele.runr.CustomApp
import me.cpele.runr.R
import me.cpele.runr.infra.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Return value not needed. Just need MainViewModel init and onCleared to be called
        val viewModel = ViewModelProviders
            .of(this, CustomApp.instance.mainViewModelFactory)
            .get(MainViewModel::class.java)

        viewModel.effect.observe(this, Observer {
            val view = fragment.view ?: return@Observer

            when (val effect = it.value) {
                is MainViewModel.Effect.Message -> Snackbar.make(
                    view,
                    effect.message,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }
}
