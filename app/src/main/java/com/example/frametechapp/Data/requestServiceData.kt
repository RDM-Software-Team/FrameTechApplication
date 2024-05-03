package com.example.frametech_app.Data

import java.sql.Timestamp

data class requestServiceData(
    val requestId: String,
    val nameOfRequest: String,
    val deviceType: String,
    val dateOfRequest: Timestamp,

)
