package com.example.sergey.photogallery.data.remote.params

data class PhotosSearchParams(
        val latitude: Double,
        val longitude: Double
) : BaseParams() {
    init {
        this["lat"] = latitude.toString()
        this["lon"] = longitude.toString()
        this["extras"] = "url_q"
    }
}