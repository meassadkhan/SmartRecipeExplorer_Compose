package com.example.smartrecipeexplorer.data.repository

import com.example.smartrecipeexplorer.data.api.MealApi
import com.example.smartrecipeexplorer.data.local.dao.FavoriteDao
import com.example.smartrecipeexplorer.data.local.entity.FavoriteEntity
import com.example.smartrecipeexplorer.domain.model.Recipe
import com.example.smartrecipeexplorer.domain.repository.RecipeRepository
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val api: MealApi,
    private val dao: FavoriteDao
) : RecipeRepository {


    override suspend fun getRecipes(): List<Recipe> {

        val favorites = dao.getFavorites().map { it.id }

        return api.getRecipes().meals.map {
            val recipe = it.toRecipe()
            recipe.copy(isFavourite = favorites.contains(recipe.id.toString()))
        }
    }

    override suspend fun getRecipeDetail(id: String): Recipe {

        val favorites = dao.getFavorites().map { it.id }

        val recipe = api.getRecipeDetail(id).meals.first().toRecipe()

        return recipe.copy(isFavourite = favorites.contains(id))
    }

    override suspend fun toggleFavorite(id: String) {

        val favorites = dao.getFavorites().map { it.id }

        if (favorites.contains(id)) {
            dao.delete(id)
        } else {
            dao.insert(FavoriteEntity(id))
        }
    }
}