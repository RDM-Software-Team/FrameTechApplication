package com.example.frametechapp.Data

import android.net.Uri
import com.example.frametechapp.Pages.Condition
import com.example.frametechapp.Pages.ProductType
import java.sql.Timestamp

data class SellsReports(
    val sellID: String = "",
    var productName: String = "",
    var productDescription: String ="",
    var productImages: List<Uri?> = emptyList(),
    var status: String = "",
    var price:Double,
    val condition: Condition,
    val type: ProductType,
    val timestamp: Timestamp = Timestamp(System.currentTimeMillis())
)
