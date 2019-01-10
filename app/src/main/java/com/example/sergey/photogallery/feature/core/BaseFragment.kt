package com.example.sergey.photogallery.feature.core

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import kotlinx.coroutines.CoroutineScope
import org.koin.standalone.KoinComponent

open class BaseFragment : Fragment(), KoinComponent {
    lateinit var scope: CoroutineScope

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scope = LifecycleScope()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (scope as? LifecycleScope)?.cancel()
    }
}