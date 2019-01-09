package com.example.sergey.photogallery.data.response

import com.google.gson.annotations.SerializedName

enum class Status {
    @SerializedName("ok")
    OK,

    @SerializedName("fail")
    ERROR
}