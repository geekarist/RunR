package me.cpele.runr.infra.viewmodel

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> MutableLiveData<T>.dispatchValue(newValue: T?) {
    withContext(Dispatchers.Main) { value = newValue }
}