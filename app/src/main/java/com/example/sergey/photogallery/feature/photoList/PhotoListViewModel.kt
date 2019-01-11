package com.example.sergey.photogallery.feature.photoList

import android.arch.lifecycle.MutableLiveData
import android.location.Location
import android.location.LocationManager
import com.example.sergey.photogallery.data.database.ApplicationDataBase
import com.example.sergey.photogallery.data.local.PreferenceManager
import com.example.sergey.photogallery.data.pojo.ErrorLoadingState
import com.example.sergey.photogallery.data.pojo.LoadingCompleteState
import com.example.sergey.photogallery.data.pojo.LoadingDataState
import com.example.sergey.photogallery.data.pojo.LoadingStartState
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
        locationManager: LocationManager,
        private val serviceApi: ServiceApi,
        private val preferenceManager: PreferenceManager,
        private val applicationDataBase: ApplicationDataBase
) : BaseViewModel() {

    companion object {
        private const val LAST_LATITUDE = "last_latitude"
        private const val LAST_LONGITUDE = "last_longitude"
        private const val DEFAULT_LAST_LOCATION_VALUE = 0f
    }

    val locationLiveData = LocationLiveData(locationManager)
    val photosLoadingState = MutableLiveData<LoadingDataState>()

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

    override fun errorRunInScope() {
        photosLoadingState.postValue(ErrorLoadingState())
    }

    private fun isValidLocation(latitude: Float, longitude: Float): Boolean {
        return (latitude * 1e6).toInt() > 0 && (longitude * 1e6).toInt() > 0
    }

    fun loadPhotos(location: Location?, force: Boolean = false) = runInScope {
        mutex.withLock {
            location.takeIf { it != null }?.let {
                val photosSearchDeferred = when {
                    needLoadFromRemote(it, force) -> loadFromRemoteAsync(it)
                    !isLoadedFromDb -> loadFromDbAsync()
                    else -> return@withLock
                }
                val data = photosSearchDeferred.await()
                val state = when (data.status) {
                    Status.OK -> LoadingCompleteState(data.photosInfo)
                    Status.ERROR -> ErrorLoadingState()
                }
                photosLoadingState.postValue(state)
            }
        }
    }

    private fun needLoadFromRemote(location: Location, force: Boolean): Boolean {
        return force || lastLocation == null || lastLocation!!.distanceTo(location) > 5 * 1000
    }

    private fun loadFromRemoteAsync(location: Location) = GlobalScope.async {
        photosLoadingState.postValue(LoadingStartState())

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

    private fun loadFromDbAsync() = GlobalScope.async {
        photosLoadingState.postValue(LoadingStartState())

        isLoadedFromDb = true
        val photosInDatabase = applicationDataBase.getPhotoDao().getPhotos()
        val photosInfo = PhotosInfo(perPage = photosInDatabase.size, photos = photosInDatabase)
        return@async PhotosSearch(photosInfo = photosInfo)
    }
}