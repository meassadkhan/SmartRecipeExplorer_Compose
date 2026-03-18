package com.example.smartrecipeexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartrecipeexplorer.domain.model.Recipe
import com.example.smartrecipeexplorer.domain.usecase.GetRecipesUseCase
import com.example.smartrecipeexplorer.domain.usecase.ToggleFavoriteUseCase
import com.example.smartrecipeexplorer.ui.state.RecipeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RecipeUiState>(RecipeUiState.Loading)
    val state: StateFlow<RecipeUiState> = _state

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery


    init {
        observeRecipes()
    }

    private fun observeRecipes() {
        viewModelScope.launch {

            _searchQuery
                .debounce(500)
                .collectLatest { query ->

                    _state.value = RecipeUiState.Loading

                    try {
                        val recipes = getRecipesUseCase()

                        val filtered = if (query.isBlank()) {
                            recipes
                        } else {
                            recipes.filter {
                                it.title.contains(query, ignoreCase = true)
                            }
                        }

                        _state.value = RecipeUiState.Success(filtered)

                    } catch (e: Exception) {
                        _state.value = RecipeUiState.Error(
                            e.message ?: "Error occurred"
                        )
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFavoriteClick(id: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(id)
            observeRecipes() // refresh list
        }
    }

    }
