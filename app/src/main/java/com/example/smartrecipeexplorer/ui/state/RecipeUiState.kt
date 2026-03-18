package com.example.smartrecipeexplorer.ui.state

import com.example.smartrecipeexplorer.domain.model.Recipe

sealed class RecipeUiState {

    object Loading : RecipeUiState()

    data class Success(
        val data: List<Recipe>
    ) : RecipeUiState()

    data class Error(
        val message: String
    ) : RecipeUiState()
}