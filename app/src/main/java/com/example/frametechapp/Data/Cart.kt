package com.example.frame_tech_app.Data

import com.example.frametech_app.Data.items

data class Cart(
    val cardId: String,
    val items: List<items> = emptyList(),//This will be used to store items in the cart
    val onPay: () -> Unit,//Action for paying for the items selected
    val onModify: () -> Unit,//Action for changing item details
    val onDelete:() -> Unit,
    val total: Double
)
