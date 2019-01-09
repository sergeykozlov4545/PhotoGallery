package com.example.sergey.photogallery.data.remote

import com.example.sergey.photogallery.data.response.PhotosSearch
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ServiceApi {
    @GET("rest/?method=flickr.photos.search")
    fun getNearPhotos(@QueryMap params: Map<String, String>): Deferred<PhotosSearch>
}