package com.example.sergey.photogallery.data.network.response

import com.google.gson.annotations.SerializedName

data class PhotosPageInfo(
        @SerializedName("photo")
        val photos: List<Photo>
)