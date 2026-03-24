package com.example.smartrecipeexplorer.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.smartrecipeexplorer.domain.model.Recipe
import com.example.smartrecipeexplorer.ui.components.EmptyView
import com.example.smartrecipeexplorer.ui.components.ErrorView
import com.example.smartrecipeexplorer.ui.components.LoadingView
import com.example.smartrecipeexplorer.ui.components.RecipeCard
import com.example.smartrecipeexplorer.ui.enums.RecipeFilter
import com.example.smartrecipeexplorer.ui.state.RecipeUiState
import com.example.smartrecipeexplorer.ui.viewmodel.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val recipeState by viewModel.state.collectAsState()
    val visibleList by viewModel.visibleRecipes.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.filter.collectAsState()

    val isRefreshing = recipeState is RecipeUiState.Loading

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            TopAppBar(
                title = {
                    Text("Smart Recipe Explorer")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate("favorites")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites"
                        )
                    }
                }

            )
        }
    ) { padding ->


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .pullRefresh(pullRefreshState)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.End)
                ) {

                    FilterChip(
                        selected = selectedFilter == RecipeFilter.ALL,
                        onClick = { viewModel.onFilterChange(RecipeFilter.ALL) },
                        label = { Text("All") }
                    )

                    FilterChip(
                        selected = selectedFilter == RecipeFilter.FAVORITES,
                        onClick = { viewModel.onFilterChange(RecipeFilter.FAVORITES) },
                        label = { Text("Favorites") }
                    )
                }


                when (recipeState) {
                    is RecipeUiState.Loading -> LoadingView()
                    is RecipeUiState.Success -> {
                        Log.i("visibleList", "HomeScreen: ${visibleList.size}")
                        RecipeList(
                            navController = navController,
                            recipes = visibleList,
                            viewModel = viewModel
                        )

                    }

                    is RecipeUiState.Error -> ErrorView(
                        (recipeState as RecipeUiState.Error).message,
                        onRetry = { viewModel.refresh() }
                    )

                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
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
            items(
                items = recipes,
                key = {it.id}

            ) { recipe ->
                RecipeCard(
                    recipe = recipe.copy(isFavourite = recipe.isFavourite),
                    onClick = { navController.navigate("detail/${recipe.id}") },
                    onFavoriteClick = {
                        viewModel.onFavoriteClick(recipe.id.toString())
                    },
                    modifier = Modifier.animateItem()

                )
            }

        }


    }
}