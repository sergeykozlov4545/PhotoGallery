package com.example.sergey.photogallery.feature.photoList

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import org.koin.standalone.KoinComponent

class LocationLiveData(
        private val locationManager: LocationManager
) : MutableLiveData<Location>(), KoinComponent {

    private val locationListener = object : LocationListener {
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String?) {}

        override fun onProviderDisabled(provider: String?) {}

        override fun onLocationChanged(location: Location?) {
            postLocation(location)
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

    private fun postLocation(location: Location?) {
        location.takeIf { it != null }?.let { postValue(it) }
    }
}