package com.example.smartrecipeexplorer.data.di.module

import com.example.smartrecipeexplorer.data.repository.FakeRecipeRepositoryImpl
import com.example.smartrecipeexplorer.domain.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRecipeRepository(): RecipeRepository {
        return FakeRecipeRepositoryImpl()
    }

}