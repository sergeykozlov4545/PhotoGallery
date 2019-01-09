package com.example.sergey.photogallery.data.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.sergey.photogallery.data.remote.ServiceApi
import com.example.sergey.photogallery.data.remote.params.PhotosSearchParams
import com.example.sergey.photogallery.data.response.PhotosSearch

interface PhotosRepository {
    suspend fun getNearPhotos(
            latitude: Double,
            longitude: Double,
            force: Boolean = false
    ): LiveData<PhotosSearch>
}

class PhotosRepositoryImpl(
        private val serviceApi: ServiceApi
) : PhotosRepository {

    private var photosData: MutableLiveData<PhotosSearch>? = null

    override suspend fun getNearPhotos(
            latitude: Double,
            longitude: Double,
            force: Boolean
    ): LiveData<PhotosSearch> {
        return photosData.takeIf { it != null && !force }
                ?: run {
                    return@run MutableLiveData<PhotosSearch>().apply {
                        value = serviceApi.getNearPhotos(PhotosSearchParams(latitude, longitude)).await()
                    }.also {
                        photosData = it
                    }
                }
    }
}