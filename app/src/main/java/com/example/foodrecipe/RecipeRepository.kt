package com.example.foodrecipe

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class RecipeRepository {

    fun searchRecipesByIngredient(
        ingredient: String,
        callback: RecipeCallback
    ) {
        val call = RetrofitClient.recipeApi.searchRecipesByIngredient(ingredient)
        Log.d("RecipeRepository", "Request URL: ${call.request().url}")
        call.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                val recipeResponse = response.body() // Get the body of the response
                if (response.isSuccessful && recipeResponse != null) {
                    val recipes = recipeResponse.hits?.map { it.recipe } ?: emptyList()
                    callback.onSuccess(recipes)
                } else {
                    // Log the error for debugging
                    Log.e("RecipeRepository", "Error in API call: ${response.code()} - ${response.message()}")

                    // Pass a generic error message to the callback
                    callback.onError("Error: Unable to fetch recipes. Please try again.")
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                // Log the error for debugging
                Log.e("RecipeRepository", "API call failed", t)

                // Pass a generic error message to the callback
                callback.onError("Error: ${t.message ?: "Unknown error"}")
            }
        })
    }

    interface RecipeCallback {
        fun onSuccess(recipes: List<Recipe>)
        fun onError(message: String)
    }
}
