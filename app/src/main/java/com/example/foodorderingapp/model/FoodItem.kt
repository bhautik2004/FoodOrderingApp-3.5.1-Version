package com.example.foodorderingapp.model

data class FoodItem(
    var id:String,
    var foodName: String,
    var foodPrice: String,
    var foodImage: ByteArray,
    var foodDescription: String
)
