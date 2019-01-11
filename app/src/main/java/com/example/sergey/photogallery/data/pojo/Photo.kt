package com.example.sergey.photogallery.data.pojo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "photos")
data class Photo(
        @PrimaryKey @SerializedName("id") val id: Long,
        @SerializedName("owner") val owner: String,
        @SerializedName("title") val title: String,
        @SerializedName("url_q") val url: String
)