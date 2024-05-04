package com.example.frame_tech_app.Data

import android.net.Uri

data class profileData(//changed val to var this data will be updated by the user if need be
    var customerId: String,
    var firstname: String,
    var lastname: String,
    var cellNumber: String,
    var dob: String,
    var email:String,
    var address:String,
    var gender: String = "",
    var cardDetails: List<CardDetails> = emptyList(),//This is the object for holding card details
    var profileImage: Uri? = null,
)
