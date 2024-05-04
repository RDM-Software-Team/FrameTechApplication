package com.example.frametechapp.Pages

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frame_tech_app.Data.profileData
import com.example.frametechapp.R
import coil.compose.rememberImagePainter

val userProfile = listOf(//preview Data to use in the app
    profileData(
        "cus_8391",
        "Ronewa",
        "Maselesele",
        "0671829305",
        "12/23/2023",
        "www.sdfadvsofijsdv@gmail.com",
        "12 street,Soweto,Gauteng",
        "Male"
    )
)
var changedProfile = listOf<profileData>()
@Preview(showBackground = true)
@Composable
fun Profile(){
    //Text(text = "Profile")
    getProfileData(userProfile)
    ProfileFrame(getProfileData(userProfile))
    getProfileData(changedProfile).forEach {
        Text(it.firstname)
    }


}
fun getProfileData(data:List<profileData>):List<profileData>{//this function will be used to get the data from the api/database/local storage
    return data
}

@Composable
fun ProfileFrame(profile:List<profileData>){
    val context = LocalContext.current
    val isEnable = remember { mutableStateOf(false)}//this variable will be used to enable and disable the text fields
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var cellNumber by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }//This will be storing the uri for the image chosen from the device gallery
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri //this set the uri to the imageUri
    }

    val currentProfile = remember { mutableStateOf(profile) }

    Column {
        LazyRow(modifier = Modifier
            .fillMaxWidth()
        ) {
            items(currentProfile.value) { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color.LightGray)
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = if(imageUri != null) rememberImagePainter(imageUri) else  painterResource(id = R.drawable.default_person),
                        contentDescription = null,
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(50))
                            .border(5.dp, Color.White)
                            .clickable {
                                launcher.launch("image/*")//this will launch the gallery to choose an image
                            }
                    )
                    Text(
                        text = user.email,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
        Divider(modifier = Modifier
            .padding(10.dp)
            .background(Color.Gray))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                          isEnable.value = true
                          Toast.makeText(context, "Edit Profile", Toast.LENGTH_SHORT).show()
                },
                colors = IconButtonDefaults.iconButtonColors(Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.edit_pen),
                    contentDescription = null
                )
            }
        }
        LazyColumn{
            items(currentProfile.value) { user ->
                OutlinedTextField(
                    value = if (!isEnable.value) user.firstname else firstname,
                    onValueChange = {
                        if(isEnable.value) firstname = it else user.firstname = it
                    },
                    label = { Text(text = "Enter Firstname") },
                    enabled = isEnable.value
                )

                OutlinedTextField(
                    value = if (!isEnable.value) user.lastname else lastname,
                    onValueChange = {
                        if(isEnable.value) lastname = it else user.lastname = it
                    },
                    label = { Text(text = "Enter Lastname")},
                    enabled = isEnable.value
                )

                OutlinedTextField(
                    value = if (!isEnable.value) user.dob else dob,
                    onValueChange = {
                        if(isEnable.value) dob = it else user.dob = it
                    },
                    label = { Text(text = "Enter dob")},
                    enabled = isEnable.value
                )

                OutlinedTextField(
                    value = if (!isEnable.value) user.email else email,
                    onValueChange = {
                        if(isEnable.value) email = it else user.email = it
                    },
                    label = { Text(text = "Enter Email")},
                    enabled = isEnable.value
                )

                OutlinedTextField(
                    value = if (!isEnable.value) user.gender else gender,
                    onValueChange = {
                        if(isEnable.value) gender = it else user.gender = it
                    },
                    label = { Text(text = "Enter Gender")},
                    enabled = isEnable.value
                )

                OutlinedTextField(
                    value = if (!isEnable.value) user.address else address,
                    onValueChange = {
                        if(isEnable.value) address = it else user.address = it
                    },
                    label = { Text(text = "Enter Address")},
                    enabled = isEnable.value
                )

                OutlinedTextField(
                    value = if (!isEnable.value) user.cellNumber else cellNumber,
                    onValueChange = {
                        if(isEnable.value) cellNumber = it else user.cellNumber = it
                    },
                    label = { Text(text = "Enter PhoneNumber")},
                    enabled = isEnable.value
                )
                Button(onClick = {

                       // updateUserProfile(user.customerId,firstname,lastname,cellNumber,dob,imageUri,email,address,gender)//This function will be used to update the user profile.
                    // Capture the updated profile list returned from updateUserProfile function
                    if(firstname.isEmpty() || lastname.isEmpty() || cellNumber.isEmpty() || dob.isEmpty() || email.isEmpty() || address.isEmpty() || gender.isEmpty()){
                        currentProfile.value = updateUserProfile(
                            user.customerId,
                            user.firstname,
                            user.lastname,
                            user.cellNumber,
                            user.dob,
                            user.profileImage,
                            user.email,
                            user.address,
                            user.gender
                        )//If no changes are made to the profile, do not update it
                    }else{
                    currentProfile.value = updateUserProfile(
                        user.customerId,
                        firstname,
                        lastname,
                        cellNumber,
                        dob,
                        imageUri,
                        email,
                        address,
                        gender
                    )
                    }

                    // Update the profile variable with the updated profile list
                    //currentProfile.value = updatedProfileList
                    //changedProfile = updatedProfileList//Global access to the updated profile list
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()


                    isEnable.value = false
                },
                    enabled = isEnable.value,
                    modifier = Modifier
                        .padding(10.dp)
                        .width(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color.Blue)

                ) {
                    Text(text = "Save")
                }
            }

        }
    }
}
fun updateUserProfile(//This function will be used to update the user profile
    userId: String,
    newFirstName: String? = null,
    newLastName: String? = null,
    newPhoneNumber: String? = null,
    newDob: String? = null,
    newImage: Uri? = null,
    newEmail: String? = null,
    newAddress: String? = null,
    newGender: String? = null
): List<profileData> {
    return userProfile.map { profile ->
        if (profile.customerId == userId) {
            profile.copy(
                firstname = newFirstName ?: profile.firstname,
                lastname = newLastName ?: profile.lastname,
                cellNumber = newPhoneNumber ?: profile.cellNumber,
                email = newEmail ?: profile.email,
                dob = newDob ?: profile.dob,
                profileImage = newImage ?: profile.profileImage,
                address = newAddress ?: profile.address,
                gender = newGender ?: profile.gender
            )
        } else {
            profile
        }
    }
}