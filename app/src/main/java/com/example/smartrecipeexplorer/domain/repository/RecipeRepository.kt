package com.example.smartrecipeexplorer.domain.repository

import com.example.smartrecipeexplorer.domain.model.Recipe

interface RecipeRepository {

    suspend fun getRecipes(): List<Recipe>
    suspend fun getRecipeDetail(id: String): Recipe

    suspend fun toggleFavorite(id: String)




}