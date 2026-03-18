package com.example.smartrecipeexplorer.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartrecipeexplorer.ui.components.DetailContent
import com.example.smartrecipeexplorer.ui.components.ErrorView
import com.example.smartrecipeexplorer.ui.components.LoadingView
import com.example.smartrecipeexplorer.ui.state.DetailUiState
import com.example.smartrecipeexplorer.ui.viewmodel.DetailViewModel

@Composable
fun DetailScreen(
    recipeId: String,
    viewModel: DetailViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRecipe(recipeId)
    }

    when (state) {

        is DetailUiState.Loading -> {
            LoadingView()
        }

        is DetailUiState.Success -> {
            val recipe = (state as DetailUiState.Success).recipe
            DetailContent(recipe)
        }

        is DetailUiState.Error -> {
            val message = (state as DetailUiState.Error).message
            ErrorView(message)
        }
    }
}