package com.example.sergey.photogallery.data.pojo

import com.google.gson.annotations.SerializedName

data class Photo(
        @SerializedName("id") val id: Long,
        @SerializedName("owner") val owner: String,
        @SerializedName("title") val title: String,
        @SerializedName("url_q") val url: String
)