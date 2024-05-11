package com.example.frametechapp.Data

import android.net.Uri

data class RepairRequest(//This class will be used to store request by the user
    var customerID: String,
    var problemName:String,
    var problemDescription: String,
    var profileImage: Uri? = null,
    )
