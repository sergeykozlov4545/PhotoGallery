package com.example.sergey.photogallery.data.repository

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.example.sergey.photogallery.exception.NotFoundGpsProvider
import com.example.sergey.photogallery.exception.NotFoundLocationManagerException

interface LocationRepository {
    fun getLastLocation(): LiveData<Location>
}

class LocationLiveData(
        private val locationManager: LocationManager
) : MutableLiveData<Location>() {

    private val locationListener = object : LocationListener {
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String?) {}

        override fun onProviderDisabled(provider: String?) {}

        override fun onLocationChanged(location: Location?) {
            value = location
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000L, 10000.toFloat(), locationListener)
    }

    override fun onInactive() {
        super.onInactive()
        locationManager.removeUpdates(locationListener)
    }
}

class LocationRepositoryImpl(
        context: Context
) : LocationRepository {
    private var locationLiveData: LocationLiveData

    init {
        val locationManager: LocationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                        ?: throw NotFoundLocationManagerException()
        if (!locationManager.allProviders.contains(LocationManager.GPS_PROVIDER)) {
            throw NotFoundGpsProvider()
        }
        locationLiveData = LocationLiveData(locationManager)
    }

    override fun getLastLocation() = locationLiveData
}
