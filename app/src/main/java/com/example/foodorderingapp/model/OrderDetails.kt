package com.example.foodorderingapp.model

data class OrderDetails(
    val orderId: Long,
    val username: String,
    val email: String,
    val phone: String,
    val address: String,
    val date: String,
    val totalAmount: String
)