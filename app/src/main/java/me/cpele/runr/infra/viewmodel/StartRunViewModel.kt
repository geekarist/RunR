package me.cpele.runr.infra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.cpele.runr.domain.usecase.StartRunUseCase

class StartRunViewModel(
    private val startRunUseCase: StartRunUseCase
) : ViewModel() {

    fun onStartRunClicked() = viewModelScope.launch {
        startRunUseCase.execute(100)
    }
}