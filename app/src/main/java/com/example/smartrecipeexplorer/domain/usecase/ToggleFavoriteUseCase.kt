package com.example.smartrecipeexplorer.domain.usecase

import com.example.smartrecipeexplorer.domain.repository.RecipeRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(id: String) {
        repository.toggleFavorite(id)
    }
}