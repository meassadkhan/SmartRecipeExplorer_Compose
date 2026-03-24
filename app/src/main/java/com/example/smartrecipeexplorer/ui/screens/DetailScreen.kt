package com.example.smartrecipeexplorer.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.smartrecipeexplorer.ui.components.DetailContent
import com.example.smartrecipeexplorer.ui.components.ErrorView
import com.example.smartrecipeexplorer.ui.components.LoadingView
import com.example.smartrecipeexplorer.ui.state.DetailUiState
import com.example.smartrecipeexplorer.ui.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    recipeId: String,
    viewModel: DetailViewModel = hiltViewModel(),
    navController: NavController
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRecipe(recipeId)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        when (state) {

            is DetailUiState.Loading -> {
                LoadingView(
                    modifier = Modifier.padding(paddingValues)
                )
            }


            is DetailUiState.Success -> {
                val recipe = (state as DetailUiState.Success).recipe
                DetailContent(
                   recipe = recipe,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is DetailUiState.Error -> {
                val message = (state as DetailUiState.Error).message
                ErrorView(
                   message =  message,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}