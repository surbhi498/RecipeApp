package com.example.foodrecipe

data class Recipe(
    val label: String,
    val ingredients: List<String>,
    val url: String
)
data class Ingredient(
    val text: String,
    val quantity: Double,
    val measure: String,
    val food: String,
    val weight: Double,
    val foodCategory: String,
    val foodId: String,
    val image: String
)