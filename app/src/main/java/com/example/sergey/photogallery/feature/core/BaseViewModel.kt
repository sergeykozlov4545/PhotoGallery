package com.example.sergey.photogallery.feature.core

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private val scope = LifecycleScope()

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    protected fun runInScope(block: suspend CoroutineScope.() -> Unit) {
        scope.launch(block = block)
    }

    fun stopScope() {
        scope.cancel()
    }
}