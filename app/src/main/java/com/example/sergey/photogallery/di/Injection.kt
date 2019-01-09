package com.example.sergey.photogallery.di

import android.app.Application
import com.example.sergey.photogallery.BuildConfig
import com.example.sergey.photogallery.data.remote.ServiceApiManager
import com.example.sergey.photogallery.data.repository.LocationRepository
import com.example.sergey.photogallery.data.repository.LocationRepositoryImpl
import com.example.sergey.photogallery.data.repository.PhotosRepository
import com.example.sergey.photogallery.data.repository.PhotosRepositoryImpl
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger
import org.koin.dsl.context.ModuleDefinition
import org.koin.dsl.module.module
import org.koin.log.EmptyLogger
import org.koin.standalone.KoinComponent

object Injection : KoinComponent {

    private val rootModule = module {
        single { ServiceApiManager.createService() }

        initRepositories()
    }

    fun start(application: Application) {
        val logger = if (BuildConfig.DEBUG) AndroidLogger() else EmptyLogger()
        application.startKoin(application, listOf(rootModule), logger = logger)
    }
}

fun ModuleDefinition.initRepositories() {
    single<LocationRepository> { LocationRepositoryImpl(get()) }
    single<PhotosRepository> { PhotosRepositoryImpl(get()) }
}