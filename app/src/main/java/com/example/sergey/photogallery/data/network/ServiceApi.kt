package com.example.sergey.photogallery.data.network

import com.example.sergey.photogallery.data.network.response.PhotosRecent
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ServiceApi {
    @GET("services/rest")
    fun getPhotosRecent(@QueryMap params: Map<String, String>): Deferred<PhotosRecent>
}