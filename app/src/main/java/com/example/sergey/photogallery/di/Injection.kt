package com.example.sergey.photogallery.di

import android.app.Application
import com.example.sergey.photogallery.BuildConfig
import com.example.sergey.photogallery.data.remote.ServiceApiManager
import com.example.sergey.photogallery.feature.core.LifecycleScope
import com.example.sergey.photogallery.feature.photoList.LocationViewModel
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
    viewModel { (scope: LifecycleScope) -> LocationViewModel(scope, androidContext()) }
    viewModel { (scope: LifecycleScope) -> PhotoListViewModel(scope, get()) }
}