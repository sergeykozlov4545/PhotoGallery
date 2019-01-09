package com.example.sergey.photogallery.di

import android.app.Application
import com.example.sergey.photogallery.BuildConfig
import com.example.sergey.photogallery.data.remote.ServiceApiManager
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger
import org.koin.dsl.module.module
import org.koin.log.EmptyLogger
import org.koin.standalone.KoinComponent

object Injection : KoinComponent {

    private val rootModule = module {
        single { ServiceApiManager.createService() }

    }

    fun start(application: Application) {
        val logger = if (BuildConfig.DEBUG) AndroidLogger() else EmptyLogger()
        application.startKoin(application, listOf(rootModule), logger = logger)
    }
}