package com.example.foodrecipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface RecipeApi {

    @GET("api/recipes/v2")
    fun searchRecipesByIngredient(
        @Query("q") query: String,
        @Query("type") type: String="public",
        @Query("app_id") appId: String = "bb8b87b7",
        @Query("app_key") appKey: String = "0d75ff3e9d3564c72a755a16ecdcb9df"
    ): Call<RecipeResponse>
}
