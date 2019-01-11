package com.example.sergey.photogallery.feature.photoList

import android.arch.lifecycle.MutableLiveData
import android.location.Location
import android.location.LocationManager
import com.example.sergey.photogallery.data.database.ApplicationDataBase
import com.example.sergey.photogallery.data.local.PreferenceManager
import com.example.sergey.photogallery.data.remote.ServiceApi
import com.example.sergey.photogallery.data.remote.params.PhotosSearchParams
import com.example.sergey.photogallery.data.response.PhotosInfo
import com.example.sergey.photogallery.data.response.PhotosSearch
import com.example.sergey.photogallery.data.response.Status
import com.example.sergey.photogallery.feature.core.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PhotoListViewModel(
        private val serviceApi: ServiceApi,
        private val preferenceManager: PreferenceManager,
        private val applicationDataBase: ApplicationDataBase
) : BaseViewModel() {

    companion object {
        private const val LAST_LATITUDE = "last_latitude"
        private const val LAST_LONGITUDE = "last_longitude"
        private const val DEFAULT_LAST_LOCATION_VALUE = 0f
    }

    var photos = MutableLiveData<PhotosSearch>()

    private var lastLocation: Location? = null

    private val mutex = Mutex()

    private var isLoadedFromDb = false

    init {
        val latitude = preferenceManager.getFloat(LAST_LATITUDE, DEFAULT_LAST_LOCATION_VALUE)
        val longitude = preferenceManager.getFloat(LAST_LONGITUDE, DEFAULT_LAST_LOCATION_VALUE)

        if (isValidLocation(latitude, longitude)) {
            lastLocation = Location(LocationManager.GPS_PROVIDER).apply {
                setLatitude(latitude.toDouble())
                setLongitude(longitude.toDouble())
            }
            loadPhotos(lastLocation)
        }
    }

    private fun isValidLocation(latitude: Float, longitude: Float): Boolean {
        return (latitude * 1e6).toInt() > 0 && (longitude * 1e6).toInt() > 0
    }

    fun loadPhotos(location: Location?, force: Boolean = false) = runInScope {
        mutex.withLock {
            location.takeIf { it != null }?.let {
                val photosSearchDeferred = when {
                    needLoadFromRemote(it, force) -> loadFromRemote(it)
                    !isLoadedFromDb -> loadFromDb()
                    else -> return@withLock
                }
                photos.postValue(photosSearchDeferred.await())
            }
        }
    }

    private fun needLoadFromRemote(location: Location, force: Boolean): Boolean {
        return force || lastLocation == null || lastLocation!!.distanceTo(location) > 5 * 1000
    }

    private fun loadFromRemote(location: Location) = GlobalScope.async {
        applicationDataBase.getPhotoDao().clearPhotos()
        val data = serviceApi.getNearPhotos(PhotosSearchParams(location.latitude, location.longitude)).await()
        return@async data.apply {
            if (status == Status.OK) {
                photosInfo?.photos?.let {
                    applicationDataBase.getPhotoDao().insertPhotos(it)
                }
                updateLastLocation(location)
            }
        }
    }

    private fun updateLastLocation(location: Location) {
        lastLocation = location
        preferenceManager.putFloat(LAST_LATITUDE, location.latitude.toFloat())
        preferenceManager.putFloat(LAST_LONGITUDE, location.longitude.toFloat())
    }

    private fun loadFromDb() = GlobalScope.async {
        isLoadedFromDb = true
        val photosInDatabase = applicationDataBase.getPhotoDao().getPhotos()
        val photosInfo = PhotosInfo(perPage = photosInDatabase.size, photos = photosInDatabase)
        return@async PhotosSearch(photosInfo = photosInfo)
    }
}