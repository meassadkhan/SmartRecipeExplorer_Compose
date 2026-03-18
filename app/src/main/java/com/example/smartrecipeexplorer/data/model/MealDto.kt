package com.example.smartrecipeexplorer.data.model

import com.example.smartrecipeexplorer.domain.model.Recipe

data class MealDto(
    val idMeal: String,
    val strMeal: String,
    val strInstructions: String,
    val strMealThumb: String
) {
    fun toRecipe(): Recipe = Recipe(
        id = idMeal.toInt(),
        title = strMeal,
        description = strInstructions,
        imageUrl = strMealThumb
    )
}