package com.example.frametechapp.Data

data class Order(
    val orderId: Int,
    val customerName: String,
    val orderDate: String, // Use String or LocalDate for date formatting
    val totalPrice: Double,
    val customerId: Int?,
    val cartId: Int?
)
