package com.example.smartrecipeexplorer.domain.repository

import com.example.smartrecipeexplorer.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

     fun getRecipes(): Flow<List<Recipe>>
     fun getRecipeDetail(id: String): Flow<Recipe>

    suspend fun toggleFavorite(id: String)




}