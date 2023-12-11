package com.example.foodorderingapp.model

data class Order(
    val orderId: Long,
    val username: String,
    val totalAmount: String,
    val date: String,
    val emai: String
)