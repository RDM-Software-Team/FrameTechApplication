package com.example.frametechapp.Data

import android.net.Uri
import java.sql.Timestamp

data class SellsReports(
    val sellID: String = "",
    var productName: String = "",
    var productDescription: String ="",
    var productImages: List<Uri?> = emptyList(),
    var status: String = "",
    val timestamp: Timestamp = Timestamp(System.currentTimeMillis())
)
