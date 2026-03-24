package com.example.smartrecipeexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartrecipeexplorer.domain.model.Recipe
import com.example.smartrecipeexplorer.domain.usecase.GetRecipesUseCase
import com.example.smartrecipeexplorer.domain.usecase.ToggleFavoriteUseCase
import com.example.smartrecipeexplorer.ui.enums.RecipeFilter
import com.example.smartrecipeexplorer.ui.state.RecipeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RecipeUiState>(RecipeUiState.Loading)
    val state: StateFlow<RecipeUiState> = _state


    private var allRecipes: List<Recipe> = emptyList()

    private val _visibleRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val visibleRecipes: StateFlow<List<Recipe>> = _visibleRecipes


    private val _filter = MutableStateFlow(RecipeFilter.ALL)
    val filter: StateFlow<RecipeFilter> = _filter

    private var currentPage = 0
    private val pageSize = 5
    private var isLoadingMore = false

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery


    init {
        observeRecipes()
    }

    private fun observeRecipes() {
        viewModelScope.launch {

            combine(
                getRecipesUseCase(),
                _searchQuery.debounce(500),
                _filter
            ) { recipes, query, filter ->

                var result = recipes

                // Apply search
                if (query.isNotBlank()) {
                    result = result.filter {
                        it.title.contains(query, ignoreCase = true)
                    }
                }

                // Apply filter
                result = when (filter) {
                    RecipeFilter.ALL -> result
                    RecipeFilter.FAVORITES -> result.filter { it.isFavourite }
                }

                result
            }
                .onStart {
                    _state.value = RecipeUiState.Loading
                }
                .catch {
                    _state.value = RecipeUiState.Error("Error")
                }
                .collect { filtered ->

                    allRecipes = filtered
                    currentPage = 0
                    _visibleRecipes.value = emptyList()

                    loadNextPage()
                    _state.value = RecipeUiState.Success(filtered)
                }
        }
    }

    fun loadNextPage() {
        if (isLoadingMore) return

        val start = currentPage * pageSize
        if (start >= allRecipes.size) return

        isLoadingMore = true

        val nextItems = allRecipes
            .drop(start)
            .take(pageSize)

        _visibleRecipes.value = _visibleRecipes.value + nextItems

        currentPage++
        isLoadingMore = false

    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFavoriteClick(id: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(id)
        }
    }

    fun refresh() {
        observeRecipes()
    }

    fun onFilterChange(newFilter: RecipeFilter) {
        _filter.value = newFilter
    }

}
