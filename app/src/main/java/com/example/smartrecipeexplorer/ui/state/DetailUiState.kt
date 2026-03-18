package com.example.smartrecipeexplorer.ui.state

import com.example.smartrecipeexplorer.domain.model.Recipe

sealed class DetailUiState {

    object Loading : DetailUiState()

    data class Success(val recipe: Recipe) : DetailUiState()

    data class Error(val message: String) : DetailUiState()
}