package com.example.sergey.photogallery.widget

import android.support.annotation.UiThread
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.sergey.photogallery.exception.HolderFactoryNotFoundException

typealias HolderClick = (View) -> Unit

abstract class SingleHolder<T>(
        open val view: View
) : RecyclerView.ViewHolder(view) {
    abstract var data: T
    var onClick: HolderClick? = null
}

interface HolderFactory<T> {
    fun create(parent: ViewGroup, viewType: Int): SingleHolder<T>
}

open class SingleHolderAdapter<T> : RecyclerView.Adapter<SingleHolder<T>>() {
    var data: List<T>? = null
        @UiThread set(value) {
            // TODO: DiffUtil
            field = value
            notifyDataSetChanged()
        }

    var holderFactory: HolderFactory<T>? = null
    var onClick: HolderClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleHolder<T> {
        return holderFactory?.create(parent, viewType)?.apply {
            onClick = this@SingleHolderAdapter.onClick
        } ?: throw HolderFactoryNotFoundException()
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onBindViewHolder(holder: SingleHolder<T>, position: Int) {
        holder.data = data!![position]
    }
}
