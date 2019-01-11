package com.example.sergey.photogallery.data.local

import android.content.Context

interface PreferenceManager {
    fun putInt(key: String, value: Int)
    fun getInt(key: String, defValue: Int): Int
    fun putString(key: String, value: String)
    fun getString(key: String, defValue: String): String
    fun putFloat(key: String, value: Float)
    fun getFloat(key: String, defValue: Float): Float
}

class PreferenceManagerImpl(
        context: Context
) : PreferenceManager {

    private val preferences =
            context.getSharedPreferences("photo_gallery_preferences", Context.MODE_PRIVATE)

    override fun putInt(key: String, value: Int) =
            preferences.edit().putInt(key, value).apply()

    override fun getInt(key: String, defValue: Int) =
            preferences.getInt(key, defValue)

    override fun putString(key: String, value: String) =
            preferences.edit().putString(key, value).apply()

    override fun getString(key: String, defValue: String) =
            preferences.getString(key, defValue)!!

    override fun putFloat(key: String, value: Float) =
            preferences.edit().putFloat(key, value).apply()

    override fun getFloat(key: String, defValue: Float) =
            preferences.getFloat(key, defValue)
}