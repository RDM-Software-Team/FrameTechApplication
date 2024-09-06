package com.example.frametech_app.Data

import java.sql.Timestamp
import java.util.Date

data class items(
    val itemId: Int,
    var itemName:String,
    var itemPhotos: List<Int> = emptyList(),
    var itemDescription: List<String> = emptyList(),
    var itemPrice: Double,
    var itemCategory: String ="",
    var dateAdded: Timestamp,
    var onBuy: () -> Unit,
    var onCancel: () -> Unit
)

data class Product(
    val productId: Int,
    val pName: String,
    val description: String,
    val price: Double,
    val category: String,
    val imagePath: String? // Base64-encoded image or null
)
data class Category(
    val category: String,
    //val items: List<Product>
)