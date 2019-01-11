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
        scope.launch {
            val result = runCatching {
                block()
            }
            if (result.isFailure) {
                errorRunInScope()
            }
        }
    }

    protected open fun errorRunInScope() {}

    fun stopScope() {
        scope.cancel()
    }
}