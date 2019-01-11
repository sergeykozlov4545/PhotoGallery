package com.example.sergey.photogallery.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.sergey.photogallery.data.pojo.Photo

@Database(entities = [Photo::class], version = 1)
abstract class ApplicationDataBase : RoomDatabase() {
    abstract fun getPhotoDao(): PhotoDao
}

object ApplicationDataBaseFactory {
    private const val DATABASE_NAME = "app_data_base"

    fun build(context: Context): ApplicationDataBase {
        return Room.databaseBuilder(
                context.applicationContext,
                ApplicationDataBase::class.java,
                DATABASE_NAME
        ).build()
    }
}