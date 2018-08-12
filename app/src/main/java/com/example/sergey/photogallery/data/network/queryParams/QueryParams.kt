package com.example.sergey.photogallery.data.network.queryParams

open class QueryParams {
    val params: MutableMap<String, String> = HashMap()

    fun addParam(key: String, value: String) = params.put(key, value)
}