package com.example.smartrecipeexplorer.data.di.module

import android.content.Context
import androidx.room.Room
import com.example.smartrecipeexplorer.data.local.AppDatabase
import com.example.smartrecipeexplorer.data.local.dao.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "recipe_db"
        ).build()
    }

    @Provides
    fun provideRecipeDao(
        db: AppDatabase
    ): FavoriteDao {
        return db.favoriteDao()
    }
}