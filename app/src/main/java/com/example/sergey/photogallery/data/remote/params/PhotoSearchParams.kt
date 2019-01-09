package com.example.sergey.photogallery.data.remote.params

data class PhotoSearchParams(
        val latitude: Double,
        val longitude: Double
) : BaseParams() {
    init {
        this["lat"] = latitude.toString()
        this["lon"] = longitude.toString()
    }
}