package me.cpele.runr.infra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.cpele.runr.domain.StartRunUseCase

class MainViewModel(
    private val startRunUseCase: StartRunUseCase
) : ViewModel() {

    fun onStartRunClicked() = viewModelScope.launch {
        startRunUseCase.execute(100)
    }
}