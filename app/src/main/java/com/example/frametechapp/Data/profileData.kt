package com.example.frame_tech_app.Data

data class profileData(
    val customerId: String,
    val firstname: String,
    val lastname: String,
    val cellNumber: Number,
    val email:String,
    val address:String,
    val gender: String = "",
    val cardDetails: List<CardDetails> = emptyList(),//This is the object for holding card details
)
