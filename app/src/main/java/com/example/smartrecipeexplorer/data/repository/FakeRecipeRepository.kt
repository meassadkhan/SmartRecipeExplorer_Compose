package com.example.smartrecipeexplorer.data.repository

import com.example.smartrecipeexplorer.domain.model.Recipe
import com.example.smartrecipeexplorer.domain.repository.RecipeRepository

class FakeRecipeRepositoryImpl : RecipeRepository {

    override suspend fun getRecipes(): List<Recipe> {
        return listOf(
            Recipe(1, "Pizza", "Italian classic"),
            Recipe(2,"Burger","American favorite"),
            Recipe(3,"Sushi","Japanese dish"),
            Recipe(4,"Pasta","Creamy pasta"),
            Recipe(5,"Salad","Healthy bowl")
        )
    }
}