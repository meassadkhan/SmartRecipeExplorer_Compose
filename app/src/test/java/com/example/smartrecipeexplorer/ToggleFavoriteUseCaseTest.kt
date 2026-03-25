package com.example.smartrecipeexplorer

import com.example.smartrecipeexplorer.domain.repository.RecipeRepository
import com.example.smartrecipeexplorer.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ToggleFavoriteUseCaseTest {

    private val repository = mockk<RecipeRepository>(relaxed = true)

    private lateinit var useCase: ToggleFavoriteUseCase

    @Before
    fun setup() {
        useCase = ToggleFavoriteUseCase(repository)
    }

    @Test
    fun `should call repository toggleFavorite`() = runTest {
        useCase("123")
        coVerify {
            repository.toggleFavorite("123")
        }
    }
}