package com.example.sergey.photogallery.feature.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import com.example.sergey.photogallery.R
import com.example.sergey.photogallery.extansion.isPermissionDenied
import com.example.sergey.photogallery.extansion.requestPermission
import com.example.sergey.photogallery.extansion.showFragment
import com.example.sergey.photogallery.extansion.toast
import com.example.sergey.photogallery.feature.core.BaseActivity
import com.example.sergey.photogallery.feature.photoList.PhotoListFragment

class MainActivity : BaseActivity() {

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        if (checkLocationPermission()) {
            showDataFragment()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode != LOCATION_REQUEST_CODE
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

    private fun checkLocationPermission(): Boolean {
        val isPermissionDenied = isPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
        if (isPermissionDenied) {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE)
        }
        return !isPermissionDenied
    }

    private fun showDataFragment() {
        supportFragmentManager.showFragment(R.id.content, PhotoListFragment.TAG) {
            return@showFragment PhotoListFragment()
        }
    }
}
