package com.example.sergey.photogallery.data.network.response

import com.google.gson.annotations.SerializedName

data class Photo(
        @SerializedName("id")
        val id: Long,

        @SerializedName("owner")
        val ownerId: String,

        @SerializedName("url_s")
        val url: String
)