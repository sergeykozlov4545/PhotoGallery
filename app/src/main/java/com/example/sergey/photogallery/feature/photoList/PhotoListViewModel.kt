package com.example.sergey.photogallery.feature.photoList

import android.arch.lifecycle.MutableLiveData
import android.location.Location
import android.location.LocationManager
import com.example.sergey.photogallery.data.local.PreferenceManager
import com.example.sergey.photogallery.data.remote.ServiceApi
import com.example.sergey.photogallery.data.remote.params.PhotosSearchParams
import com.example.sergey.photogallery.data.response.PhotosSearch
import com.example.sergey.photogallery.feature.core.BaseViewModel

class PhotoListViewModel(
        private val serviceApi: ServiceApi,
        private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    companion object {
        private const val LAST_LATITUDE = "last_latitude"
        private const val LAST_LONGITUDE = "last_longitude"
        private const val DEFAULT_LAST_LOCATION_VALUE = 0f
    }

    var photos = MutableLiveData<PhotosSearch>()

    private var lastLocation: Location? = null

    init {
        val latitude = preferenceManager.getFloat(LAST_LATITUDE, DEFAULT_LAST_LOCATION_VALUE)
        val longitude = preferenceManager.getFloat(LAST_LONGITUDE, DEFAULT_LAST_LOCATION_VALUE)
        if (isValidLocation(latitude, longitude)) {
            lastLocation = Location(LocationManager.GPS_PROVIDER).apply {
                setLatitude(latitude.toDouble())
                setLongitude(longitude.toDouble())
            }
            loadPhotos(lastLocation, true)
        }
    }

    fun loadPhotos(location: Location?, force: Boolean = false) {
        location.takeIf { it != null && needLoadPhotos(it, force) }
                ?.let {
                    lastLocation = it
                    preferenceManager.putFloat(LAST_LATITUDE, it.latitude.toFloat())
                    preferenceManager.putFloat(LAST_LONGITUDE, it.longitude.toFloat())
                    loadPhotosAsync(it)
                }
    }

    private fun isValidLocation(latitude: Float, longitude: Float): Boolean {
        return (latitude * 1e6).toInt() > 0 && (longitude * 1e6).toInt() > 0
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