package com.example.smartrecipeexplorer.data.repository

import com.example.smartrecipeexplorer.data.api.MealApi
import com.example.smartrecipeexplorer.data.local.dao.FavoriteDao
import com.example.smartrecipeexplorer.data.local.entity.FavoriteEntity
import com.example.smartrecipeexplorer.domain.model.Recipe
import com.example.smartrecipeexplorer.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val api: MealApi,
    private val dao: FavoriteDao
) : RecipeRepository {

    override  fun getRecipes(): Flow<List<Recipe>> {
        return dao.getFavorites().map {favouriteEntities->
            val favouriteIds = favouriteEntities.map { it.id }
            val response = api.getRecipes()
            response.meals.map {
                val recipe = it.toRecipe()
                recipe.copy(isFavourite = favouriteIds.contains(recipe.id.toString()))
            }
        }
    }

    override  fun getRecipeDetail(id: String): Flow<Recipe> {

        val recipeFlow = flow {
            val recipe = api.getRecipeDetail(id)
                .meals.first().toRecipe()
            emit(recipe)
        }

        val favoritesFlow = dao.getFavorites()

        return combine(recipeFlow, favoritesFlow) { recipe, favourites ->

            val favouriteIds = favourites.map { it.id }

            recipe.copy(
                isFavourite = favouriteIds.contains(id)
            )

        }
    }

    override suspend fun toggleFavorite(id: String) {
        val favorites = dao.getFavorites().first().map {
            it.id
        }

        if (favorites.contains(id)) {
            dao.delete(id)
        } else {
            dao.insert(FavoriteEntity(id))
        }
    }
}