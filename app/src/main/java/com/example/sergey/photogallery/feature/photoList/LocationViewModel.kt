package com.example.sergey.photogallery.feature.photoList

import android.content.Context
import com.example.sergey.photogallery.feature.core.BaseViewModel

class LocationViewModel(context: Context) : BaseViewModel() {
    val locationLiveData = LocationLiveData(context)
}