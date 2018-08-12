package com.example.sergey.photogallery.data.network.response

import com.google.gson.annotations.SerializedName

data class PhotosRecent(
        @SerializedName("photos")
        val photosPageInfo: PhotosPageInfo,

        @SerializedName("stat")
        val status: String
)