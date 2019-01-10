package com.example.sergey.photogallery.extansion

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

fun FragmentManager.showFragment(containerId: Int, tag: String, factory: () -> Fragment) {
    if (isStateSaved) {
        return
    }
    findFragmentByTag(tag)
            ?: beginTransaction()
                    .replace(containerId, factory(), tag)
                    .commit()
}