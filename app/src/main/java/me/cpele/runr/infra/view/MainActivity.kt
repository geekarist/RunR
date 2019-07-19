package me.cpele.runr.infra.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import me.cpele.runr.CustomApp
import me.cpele.runr.R
import me.cpele.runr.infra.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Return value not needed. Just need MainViewModel init and onCleared to be called
        ViewModelProviders
            .of(this, CustomApp.instance.mainViewModelFactory)
            .get(MainViewModel::class.java)
    }
}
