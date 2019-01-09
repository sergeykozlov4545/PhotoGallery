package com.example.sergey.photogallery.app

import android.app.Application
import com.example.sergey.photogallery.di.Injection

class PhotoGalleryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Injection.start(this)
    }
}