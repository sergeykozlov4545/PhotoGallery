package com.example.sergey.photogallery.extansion

import android.view.View

fun View?.showView() {
    takeIf { it != null }?.let { it.visibility = View.VISIBLE }
}

fun View?.hideView(onlyInvisible: Boolean = false) {
    takeIf { it != null }?.let { it.visibility = if (onlyInvisible) View.INVISIBLE else View.GONE }
}