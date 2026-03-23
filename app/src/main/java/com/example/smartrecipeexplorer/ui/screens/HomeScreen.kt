package com.example.smartrecipeexplorer.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.smartrecipeexplorer.domain.model.Recipe
import com.example.smartrecipeexplorer.ui.components.EmptyView
import com.example.smartrecipeexplorer.ui.components.ErrorView
import com.example.smartrecipeexplorer.ui.components.LoadingView
import com.example.smartrecipeexplorer.ui.components.RecipeCard
import com.example.smartrecipeexplorer.ui.state.RecipeUiState
import com.example.smartrecipeexplorer.ui.viewmodel.HomeViewModel
import androidx.compose.animation.*
import androidx.compose.runtime.*

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val recipeState by viewModel.state.collectAsState()
    val visibleList by viewModel.visibleRecipes.collectAsState()
    val query by viewModel.searchQuery.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "Smart Recipe Explorer",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = query,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(12.dp),
            placeholder = { Text("Search recipes...") },
            singleLine = true
        )


        when (recipeState) {
            is RecipeUiState.Loading -> LoadingView()
            is RecipeUiState.Success ->{
                Log.i("visibleList", "HomeScreen: ${visibleList.size}")
                RecipeList(
                navController = navController,
                recipes = visibleList,
                viewModel = viewModel
            )

            }

            is RecipeUiState.Error -> ErrorView((recipeState as RecipeUiState.Error).message)

        }
    }

}

@Composable
fun RecipeList(
    navController: NavController,
    recipes: List<Recipe>,
    viewModel: HomeViewModel
) {
    if (recipes.isEmpty()) {
        EmptyView()
    } else {
        val listState = rememberLazyListState()

        LaunchedEffect(listState) {
            snapshotFlow {
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            }.collect { lastVisibleIndex ->

                val total = listState.layoutInfo.totalItemsCount

                if (lastVisibleIndex != null && lastVisibleIndex >= total - 2) {
                    viewModel.loadNextPage()
                }
            }
        }

        LazyColumn(
            state = listState
        ) {
            items(recipes) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    onClick = { navController.navigate("detail/${recipe.id}") },
                    onFavoriteClick = { viewModel.onFavoriteClick(recipe.id.toString())
                    },
                    modifier = Modifier.animateItem()

                )
            }

        }


    }
}