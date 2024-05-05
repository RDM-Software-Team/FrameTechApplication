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
