package com.example.sergey.photogallery.feature.photoList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sergey.photogallery.R
import com.example.sergey.photogallery.app.GlideApp
import com.example.sergey.photogallery.data.pojo.Photo
import com.example.sergey.photogallery.widget.HolderFactory
import com.example.sergey.photogallery.widget.SingleHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_photo.*

class PhotoHolder(
        override val containerView: View
) : SingleHolder<Photo>(containerView), LayoutContainer {

    companion object FACTORY : HolderFactory<Photo> {
        private const val LAYOUT = R.layout.item_photo

        override fun create(parent: ViewGroup, viewType: Int): SingleHolder<Photo> {
            return PhotoHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(LAYOUT, parent, false)
            )
        }
    }

    override var data: Photo
        get() = TODO("not implemented")
        set(value) {
            GlideApp.with(itemView.context)
                    .load(value.url)
                    .placeholder(R.drawable.placeholder)
                    .into(iconView)
            titleView.text = value.title
            itemView.setOnClickListener { onClick?.invoke(it) }
        }
}