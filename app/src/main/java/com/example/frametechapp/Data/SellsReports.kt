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
    val timestamp: Timestamp = Timestamp(System.currentTimeMillis())
){
    var status: String = ""
    var price:Double = 0.0
    val condition: Condition = Condition.NEW
    val type: ProductType = ProductType.ELECTRONICS
}
data class SellListing(
    val sellId: Int,
    val description: String,
    val price: Double,
    val image1Base64: String,
    val image2Base64: String,
    val image3Base64: String
)