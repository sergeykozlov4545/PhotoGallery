package com.example.sergey.photogallery.data.remote.params

open class BaseParams : HashMap<String, String>() {
    init {
        this["api_key"] = "b1ea2013ed9473b68a272ad45cd32fd8"
        this["format"] = "json"
        this["nojsoncallback"] = "1"
    }
}