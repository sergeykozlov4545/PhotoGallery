package com.example.sergey.photogallery.feature.photoList

import android.arch.lifecycle.Observer
import android.location.Location
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sergey.photogallery.R
import com.example.sergey.photogallery.data.pojo.Photo
import com.example.sergey.photogallery.data.response.PhotosSearch
import com.example.sergey.photogallery.data.response.Status
import com.example.sergey.photogallery.extansion.hideView
import com.example.sergey.photogallery.extansion.showView
import com.example.sergey.photogallery.feature.core.BaseFragment
import com.example.sergey.photogallery.widget.SingleHolderAdapter
import kotlinx.android.synthetic.main.fragment_photo_list.*
import org.koin.android.viewmodel.ext.android.getViewModel

class PhotoListFragment : BaseFragment() {
    companion object {
        const val TAG = "photo_list_fragment"
    }

    private val locationViewModel by lazy { activity?.getViewModel<LocationViewModel>() }
    private val photoListViewModel by lazy { activity?.getViewModel<PhotoListViewModel>() }

    private val photoListAdapter = SingleHolderAdapter<Photo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.takeIf { it != null }?.let(this::observeViewModels)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_photo_list, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(photoListView) {
            layoutManager = GridLayoutManager(activity?.applicationContext, getPhotoListColumnCount())
            adapter = photoListAdapter.apply {
                holderFactory = PhotoHolder.FACTORY
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val isFinishing = activity?.isFinishing ?: true
        if (isFinishing) {
            photoListViewModel?.stopScope()
        }
    }

    private fun getPhotoListColumnCount() = resources.getInteger(R.integer.photo_list_column_count)

    private fun observeViewModels(activity: FragmentActivity) {
        locationViewModel?.locationLiveData?.observe(activity, Observer<Location> {
            photoListViewModel?.loadPhotos(it, false)
        })
        photoListViewModel?.photos?.observe(activity, Observer<PhotosSearch> { photosSearch ->
            photosSearch.takeIf { it != null }?.let { updateView(it) }
        })
    }

    private fun updateView(photosSearch: PhotosSearch) {
        when (photosSearch.status) {
            Status.OK -> showPhotoList(photosSearch)
            Status.ERROR -> showError()
        }
    }

    private fun showError() {
        progressBar.hideView()
        errorGroup.showView()
        photoListView.hideView()
    }

    private fun showPhotoList(photosSearch: PhotosSearch) {
        progressBar.hideView()
        errorGroup.hideView()
        photoListAdapter.data = photosSearch.photosInfo?.photos
        photoListView.showView()
    }
}