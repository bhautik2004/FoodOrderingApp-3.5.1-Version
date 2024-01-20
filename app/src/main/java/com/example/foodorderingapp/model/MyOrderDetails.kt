package com.example.foodorderingapp.model

data class MyOrderDetails(
    val foodName: String,
    val foodPrice: String,
    val foodQuantity: Int,
    val orderDateTime: String,
    val foodImage: ByteArray
)
