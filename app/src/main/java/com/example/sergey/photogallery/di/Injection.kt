package com.example.sergey.photogallery.di

import android.app.Application
import android.content.Context
import android.location.LocationManager
import com.example.sergey.photogallery.BuildConfig
import com.example.sergey.photogallery.data.database.ApplicationDataBaseFactory
import com.example.sergey.photogallery.data.local.PreferenceManager
import com.example.sergey.photogallery.data.local.PreferenceManagerImpl
import com.example.sergey.photogallery.data.remote.ServiceApiManager
import com.example.sergey.photogallery.exception.NotFoundGpsProviderException
import com.example.sergey.photogallery.exception.NotFoundLocationManagerException
import com.example.sergey.photogallery.feature.photoList.PhotoListViewModel
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.context.ModuleDefinition
import org.koin.dsl.module.module
import org.koin.log.EmptyLogger
import org.koin.standalone.KoinComponent

object Injection : KoinComponent {

    private val rootModule = module {
        single { ServiceApiManager.createService() }
        single {
            val locationManager = androidContext().getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                    ?: throw NotFoundLocationManagerException()
            if (!locationManager.allProviders.contains(LocationManager.GPS_PROVIDER)) {
                throw NotFoundGpsProviderException()
            }
            return@single locationManager
        }
        single<PreferenceManager> { PreferenceManagerImpl(androidContext()) }
        single { ApplicationDataBaseFactory.build(androidContext()) }

        initFeatures()
    }

    fun start(application: Application) {
        val logger = if (BuildConfig.DEBUG) AndroidLogger() else EmptyLogger()
        application.startKoin(application, listOf(rootModule), logger = logger)
    }
}

fun ModuleDefinition.initFeatures() {
    initPhotoListFeature()
}

fun ModuleDefinition.initPhotoListFeature() = module(path = "$path.photoList") {
    viewModel { PhotoListViewModel(get(), get(), get(), get()) }
}