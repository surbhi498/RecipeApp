package com.example.foodrecipe

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView

class RecipeActivity : AppCompatActivity() {

    private val recipeRepository = RecipeRepository()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        val listView: ListView = findViewById(R.id.recipeListView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listView.adapter = adapter

        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchRecipesByIngredient(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text change (optional)
                return true
            }
        })

        progressBar = findViewById(R.id.progressBar)

        // Initially load recipes for a default ingredient (e.g., "chicken")
        searchRecipesByIngredient("chicken")
    }

    private fun searchRecipesByIngredient(ingredient: String) {
        showLoading(true)

        recipeRepository.searchRecipesByIngredient(ingredient,
            object : RecipeRepository.RecipeCallback {
                override fun onSuccess(recipes: List<Recipe>) {
                    adapter.clear()
                    for (recipe in recipes) {
                        adapter.add(recipe.label)
                    }
                    showLoading(false)
                }

                override fun onError(message: String) {
                    Toast.makeText(this@RecipeActivity, "Error: $message", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            })
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}
