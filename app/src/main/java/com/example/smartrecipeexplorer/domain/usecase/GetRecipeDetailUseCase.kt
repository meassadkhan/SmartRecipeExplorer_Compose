package com.example.smartrecipeexplorer.domain.usecase

import com.example.smartrecipeexplorer.domain.model.Recipe
import com.example.smartrecipeexplorer.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipeDetailUseCase@Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(id: String): Flow<Recipe> {
        return repository.getRecipeDetail(id)
    }
}