package com.example.sergey.photogallery.data.response

import com.example.sergey.photogallery.data.pojo.Photo
import com.google.gson.annotations.SerializedName

data class PhotosSearch(
        @SerializedName("stat") val status: Status,
        @SerializedName("code") val errorCode: Int? = 0,
        @SerializedName("message") val errorMessage: String? = "",
        @SerializedName("photos") val photosInfo: PhotosInfo? = null
)

data class PhotosInfo(
        @SerializedName("page") val page: Int? = 1,
        @SerializedName("pages") val pages: Int? = 1,
        @SerializedName("perpage") val perPage: Int? = 1,
        @SerializedName("photo") val photos: List<Photo>? = ArrayList()
)