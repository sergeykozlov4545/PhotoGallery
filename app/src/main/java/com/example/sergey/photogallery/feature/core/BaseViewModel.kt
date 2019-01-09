package com.example.sergey.photogallery.feature.core

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope

open class BaseViewModel(
        private val scope: LifecycleScope
) : ViewModel(), CoroutineScope by scope {

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}