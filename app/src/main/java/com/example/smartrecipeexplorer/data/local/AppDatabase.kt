package com.example.smartrecipeexplorer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartrecipeexplorer.data.local.dao.FavoriteDao
import com.example.smartrecipeexplorer.data.local.entity.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}