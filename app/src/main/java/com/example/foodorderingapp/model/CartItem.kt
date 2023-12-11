package com.example.foodorderingapp.model

data class CartItem (

        val id: String,
        val foodId: String,
        val foodName: String,
        var foodPrice: String,
        var quantity:Int,
        val foodImage: ByteArray,
        val foodDescription: String
    )
