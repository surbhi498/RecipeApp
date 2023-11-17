package com.example.foodrecipe

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import android.util.Log
import com.google.gson.Gson

class RecipeRepository {

    fun searchRecipesByIngredient(
        ingredient: String,
        callback: RecipeCallback
    ) {
        val call = RetrofitClient.recipeApi.searchRecipesByIngredient(ingredient)
        Log.d("RecipeRepository", "Request URL: ${call.request().url}")
        call.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                if (response.isSuccessful) {
//                    val rawJson = response.body()?.let { Gson().toJson(it) }
//                    Log.d("RecipeRepository", "Raw JSON Response: $rawJson")
//                    val recipes = response.body()?.hits?.map { it.recipe } ?: emptyList()
//                    callback.onSuccess(recipes)
//                } else {
//                    // Log the error for debugging
//                    Log.e("RecipeRepository", "Error in API call: ${response.code()} - ${response.message()}")
//
//                    // Pass a generic error message to the callback
//                    callback.onError("Error: Unable to fetch recipes. Please try again.")
//                }
                    val rawJson = response.raw().body?.string()
                    Log.d("RecipeRepository", "Raw JSON response: $rawJson")

                    try {
                        val gson = Gson()
                        val recipeResponse = gson.fromJson(rawJson, RecipeResponse::class.java)
                        val recipes = recipeResponse.hits?.map { it.recipe } ?: emptyList()
                        callback.onSuccess(recipes)
                    } catch (e: Exception) {
                        Log.e("RecipeRepository", "Error parsing JSON", e)
                        callback.onError("Error: Unable to parse JSON response. Please try again.")
                    }
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
