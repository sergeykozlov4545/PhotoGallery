package com.example.sergey.photogallery

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.sergey.photogallery.data.repository.LocationRepository
import com.example.sergey.photogallery.data.repository.PhotosRepository
import com.example.sergey.photogallery.extansion.isPermissionIsDenied
import com.example.sergey.photogallery.extansion.requestPermissions
import com.example.sergey.photogallery.extansion.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), KoinComponent, CoroutineScope {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private val locationRepository by inject<LocationRepository>()
    private val photosRepository by inject<PhotosRepository>()

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE
                || permissions.size != 1
                || permissions.size != grantResults.size
                || permissions[0] != Manifest.permission.ACCESS_FINE_LOCATION) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            toast(getString(R.string.location_permission_denied))
            finish()
            return
        }
    }

    private fun checkLocationPermission() {
        if (isPermissionIsDenied(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(listOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        observeLocationLiveData()
    }

    private fun observeLocationLiveData() {
        locationRepository.getLastLocation().observe(this, Observer { location: Location? ->
            launch {
                location.takeIf { it != null }
                        ?.let {
                            photosRepository.getNearPhotos(it.latitude, it.longitude)
                        }
            }
        })
    }
}
