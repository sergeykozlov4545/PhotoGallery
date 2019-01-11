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
import com.example.sergey.photogallery.data.pojo.ErrorLoadingState
import com.example.sergey.photogallery.data.pojo.LoadingCompleteState
import com.example.sergey.photogallery.data.pojo.LoadingStartState
import com.example.sergey.photogallery.data.pojo.Photo
import com.example.sergey.photogallery.data.response.PhotosInfo
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
        photoListViewModel?.photosLoadingState?.observe(activity, Observer { state ->
            state.takeIf { it != null }?.let {
                when (it) {
                    is LoadingStartState -> startedLoading()
                    is ErrorLoadingState -> errorLoading(it.message)
                    is LoadingCompleteState<*> ->
                        if (it.data is PhotosInfo) loadingCompleted(it.data)
                }
            }
        })
    }

    private fun startedLoading() {
        progressBar.showView()
        errorGroup.hideView()
        photoListView.hideView()
    }

    private fun errorLoading(message: String) {
        progressBar.hideView()
        if (message.isNotEmpty()) {
            errorMessageView.text = message
        }
        errorGroup.showView()
    }

    private fun loadingCompleted(photosInfo: PhotosInfo) {
        progressBar.hideView()
        photoListAdapter.data = photosInfo.photos
        photoListView.showView()
    }
}