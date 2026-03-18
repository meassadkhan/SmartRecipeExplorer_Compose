package com.example.smartrecipeexplorer.domain.usecase

import com.example.smartrecipeexplorer.domain.model.Recipe
import com.example.smartrecipeexplorer.domain.repository.RecipeRepository
import javax.inject.Inject

class GetRecipeDetailUseCase@Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(id: String): Recipe {
        return repository.getRecipeDetail(id)
    }
}