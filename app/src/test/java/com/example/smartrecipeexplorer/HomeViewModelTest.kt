package com.example.smartrecipeexplorer

import com.example.smartrecipeexplorer.domain.model.Recipe
import com.example.smartrecipeexplorer.domain.usecase.GetRecipesUseCase
import com.example.smartrecipeexplorer.domain.usecase.ToggleFavoriteUseCase
import com.example.smartrecipeexplorer.ui.state.RecipeUiState
import com.example.smartrecipeexplorer.ui.viewmodel.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    private val getRecipesUseCase = mockk<GetRecipesUseCase>()
    private val toggleFavoriteUseCase = mockk<ToggleFavoriteUseCase>(relaxed = true)

    private lateinit var viewModel: HomeViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should emit success when data loaded`() = runTest {

        val fakeRecipes = listOf(
            Recipe(1, "Pizza", "", "", false)
        )

        coEvery { getRecipesUseCase() } returns kotlinx.coroutines.flow.flowOf(fakeRecipes)

        viewModel = HomeViewModel(getRecipesUseCase, toggleFavoriteUseCase)

        // ✅ Wait for coroutine to finish
        advanceUntilIdle()

        val state = viewModel.state.value

        assert(state is RecipeUiState.Success)
    }
}