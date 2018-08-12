package com.example.sergey.photogallery.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ServiceManager {
    fun getService(): ServiceApi
}

class ServiceManagerImpl : ServiceManager {

    private val serviceApi by lazy { getRetrofit().create(ServiceApi::class.java) }

    override fun getService(): ServiceApi = serviceApi

    private fun getRetrofit() = Retrofit.Builder()
            .baseUrl("api.flickr.com")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}