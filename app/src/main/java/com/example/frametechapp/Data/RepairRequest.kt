package com.example.frametechapp.Data

import android.net.Uri
import java.sql.Timestamp

data class RepairRequest(//This class will be used to store request by the user
    val repairId: Int,
    var customerID: String ="",
    var problemName:String? ="",
    var problemDescription: String,
    var profileImage: String? = null,
    val bookedDate: String

)
