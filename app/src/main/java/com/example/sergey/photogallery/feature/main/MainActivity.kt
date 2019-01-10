package com.example.sergey.photogallery.feature.main

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.example.sergey.photogallery.R
import com.example.sergey.photogallery.data.pojo.Photo
import com.example.sergey.photogallery.data.response.PhotosSearch
import com.example.sergey.photogallery.data.response.Status
import com.example.sergey.photogallery.extansion.*
import com.example.sergey.photogallery.feature.core.BaseActivity
import com.example.sergey.photogallery.feature.photoList.LocationViewModel
import com.example.sergey.photogallery.feature.photoList.PhotoHolder
import com.example.sergey.photogallery.feature.photoList.PhotoListViewModel
import com.example.sergey.photogallery.widget.SingleHolderAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : BaseActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private val locationViewModel by viewModel<LocationViewModel> { parametersOf(scope) }
    private val photoListViewModel by viewModel<PhotoListViewModel> { parametersOf(scope) }

    private val photoListAdapter = SingleHolderAdapter<Photo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(photoListView) {
            layoutManager = GridLayoutManager(applicationContext, getPhotoListColumnCount())
            adapter = photoListAdapter.apply {
                holderFactory = PhotoHolder.FACTORY
            }
        }
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

    private fun getPhotoListColumnCount() = resources.getInteger(R.integer.photo_list_column_count)

    private fun checkLocationPermission() {
        if (isPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(listOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        photoListViewModel.photos.observe(this, Observer { photoSearch ->
            photoSearch.takeIf { it != null }
                    ?.let {
                        when (it.status) {
                            Status.OK -> showPhotoList(it)
                            Status.ERROR -> showError()
                        }
                    }
        })
        locationViewModel.locationLiveData.observe(this, Observer {
            photoListViewModel.loadPhotos(it, false)
        })
    }

    private fun showError() {
        progressBar.hideView()
        photoListView.hideView()
        errorGroup.showView()
    }

    private fun showPhotoList(photosSearch: PhotosSearch) {
        progressBar.hideView()
        photoListAdapter.data = photosSearch.photosInfo?.photos
        photoListView.showView()
        errorGroup.hideView()
    }
}
