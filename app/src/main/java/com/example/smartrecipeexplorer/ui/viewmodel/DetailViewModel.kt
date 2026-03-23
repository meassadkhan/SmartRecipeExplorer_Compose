package com.example.smartrecipeexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartrecipeexplorer.domain.usecase.GetRecipeDetailUseCase
import com.example.smartrecipeexplorer.ui.state.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getRecipeDetailUseCase: GetRecipeDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val state: StateFlow<DetailUiState> = _state

    fun loadRecipe(id: String) {
        viewModelScope.launch {

            _state.value = DetailUiState.Loading

            try {
                val recipe = getRecipeDetailUseCase(id).first()
                _state.value = DetailUiState.Success(recipe)

            } catch (e: Exception) {
                _state.value = DetailUiState.Error(
                    e.message ?: "Error loading recipe"
                )
            }
        }
    }
}