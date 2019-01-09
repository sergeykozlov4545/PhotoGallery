package com.example.sergey.photogallery.feature.core

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import org.koin.standalone.KoinComponent

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), KoinComponent {
    val scope = LifecycleScope()

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}