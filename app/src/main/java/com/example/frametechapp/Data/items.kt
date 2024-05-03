package com.example.frametech_app.Data

import java.sql.Timestamp
import java.util.Date

data class items(
    val itemId: Int,
    val itemName:String,
    val itemPhotos: List<Int> = emptyList(),
    val itemDescription: List<String> = emptyList(),
    val itemPrice: Double,
    val itemCategory: String,
    val dateAdded: Timestamp,
    val onBuy: () -> Unit,
    val onCancel: () -> Unit
)
