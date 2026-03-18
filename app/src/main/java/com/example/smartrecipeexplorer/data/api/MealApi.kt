package com.example.smartrecipeexplorer.data.api

import com.example.smartrecipeexplorer.data.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("search.php?f=a")
    suspend fun getRecipes(): MealResponse

    @GET("lookup.php")
    suspend fun getRecipeDetail(
        @Query("i") id: String
    ): MealResponse
}