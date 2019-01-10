package com.example.sergey.photogallery.feature.photoList

import android.arch.lifecycle.MutableLiveData
import android.location.Location
import com.example.sergey.photogallery.data.remote.ServiceApi
import com.example.sergey.photogallery.data.remote.params.PhotosSearchParams
import com.example.sergey.photogallery.data.response.PhotosSearch
import com.example.sergey.photogallery.feature.core.BaseViewModel

class PhotoListViewModel(
        private val serviceApi: ServiceApi
) : BaseViewModel() {

    var photos = MutableLiveData<PhotosSearch>()

    private var lastLocation: Location? = null

    fun loadPhotos(location: Location?, force: Boolean = false) {
        location.takeIf { it != null && needLoadPhotos(it, force) }
                ?.let {
                    lastLocation = it
                    loadPhotosAsync(it)
                }
    }

    private fun needLoadPhotos(location: Location, force: Boolean): Boolean {
        return lastLocation == null || force || location.distanceTo(lastLocation) > 5 * 1000
    }

    private fun loadPhotosAsync(location: Location) {
        runInScope {
            val data = serviceApi.getNearPhotos(PhotosSearchParams(location.latitude, location.longitude)).await()
            photos.postValue(data)
        }
    }
}