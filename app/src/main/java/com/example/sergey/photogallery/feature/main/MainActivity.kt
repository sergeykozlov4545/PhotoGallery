package com.example.sergey.photogallery.feature.main

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.os.Bundle
import com.example.sergey.photogallery.R
import com.example.sergey.photogallery.extansion.isPermissionDenied
import com.example.sergey.photogallery.extansion.requestPermissions
import com.example.sergey.photogallery.extansion.toast
import com.example.sergey.photogallery.feature.core.BaseActivity
import com.example.sergey.photogallery.feature.photoList.LocationViewModel
import com.example.sergey.photogallery.feature.photoList.PhotoListViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : BaseActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private val locationViewModel by viewModel<LocationViewModel> { parametersOf(scope) }
    private val photoListViewModel by viewModel<PhotoListViewModel> { parametersOf(scope) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
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
        if (isPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(listOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        photoListViewModel.photos.observe(this, Observer {
            // TODO: Тут отображаем данные
            toast("Loaded")
        })
        locationViewModel.locationLiveData.observe(this, Observer {
            photoListViewModel.loadPhotos(it, false)
        })
    }
}
