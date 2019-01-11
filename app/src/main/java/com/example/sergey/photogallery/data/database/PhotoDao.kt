package com.example.sergey.photogallery.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.sergey.photogallery.data.pojo.Photo

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos")
    fun getPhotos(): List<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotos(photos: List<Photo>)

    @Query("DELETE FROM photos")
    fun clearPhotos()
}