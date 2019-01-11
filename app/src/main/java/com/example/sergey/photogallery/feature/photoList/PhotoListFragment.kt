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
import com.example.sergey.photogallery.data.pojo.*
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

        retryView.setOnClickListener {
            photoListViewModel.takeIf { viewModel -> viewModel != null }?.let { viewModel ->
                with(viewModel) {
                    loadPhotos(locationLiveData.value, true)
                }
            }
        }

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
        photoListViewModel.takeIf { it != null }?.let { viewModel ->
            with(viewModel) {
                photosLoadingState.observe(activity, Observer(this@PhotoListFragment::parseLoadingState))
                locationLiveData.observe(activity, Observer<Location> { loadPhotos(it) })
            }
        }
    }

    private fun parseLoadingState(loadingState: LoadingDataState?) {
        loadingState.takeIf { it != null }?.let {
            when (it) {
                is LoadingStartState -> startedLoading()
                is ErrorLoadingState -> errorLoading(it.message)
                is LoadingCompleteState<*> ->
                    if (it.data is PhotosInfo) loadingCompleted(it.data)
            }
        }
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