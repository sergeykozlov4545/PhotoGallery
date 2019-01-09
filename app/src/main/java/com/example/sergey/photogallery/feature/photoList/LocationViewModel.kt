package com.example.sergey.photogallery.feature.photoList

import android.content.Context
import com.example.sergey.photogallery.feature.core.BaseViewModel
import com.example.sergey.photogallery.feature.core.LifecycleScope

class LocationViewModel(
        scope: LifecycleScope,
        context: Context
) : BaseViewModel(scope) {
    val locationLiveData = LocationLiveData(context)
}