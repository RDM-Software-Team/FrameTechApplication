package com.example.frame_tech_app.Data

import java.util.Date

data class CardDetails(
    val accountHolderName: String,
    val cardNumber: Int,
    val cvv: Int,
    val expireDate: Date
)
