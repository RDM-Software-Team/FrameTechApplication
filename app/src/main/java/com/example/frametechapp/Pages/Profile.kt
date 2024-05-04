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
        167182930,
        "12/23/2023",
        "www.sdfadvsofijsdv@gmail.com",
        "12 street,Soweto,Gauteng",
        "Male"
    )
)
@Preview(showBackground = true)
@Composable
fun Profile(){
    //Text(text = "Profile")
    getProfileData(userProfile)
    ProfileFrame()


}
fun getProfileData(data:List<profileData>):List<profileData>{//this function will be used to get the data from the api/database/local storage
    return data
}

@Composable
fun ProfileFrame(profile:List<profileData> = getProfileData(userProfile)){
    val context = LocalContext.current
    val isEnable = remember { mutableStateOf(false)}//this variable will be used to enable and disable the text fields

    var imageUri by remember { mutableStateOf<Uri?>(null) }//This will be storing the uri for the image chosen from the device gallery
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri //this set the uri to the imageUri
    }
    Column {
        LazyRow(modifier = Modifier
            .fillMaxWidth()
        ) {
            items(profile) { user ->
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
            items(profile) { user ->
                OutlinedTextField(
                    value = user.firstname,
                    onValueChange = {user.firstname =  it},
                    label = { Text(text = "Enter Firstname")},
                    enabled = isEnable.value
                )

                OutlinedTextField(
                    value = user.lastname,
                    onValueChange = { user.lastname = it},
                    label = { Text(text = "Enter Lastname")},
                    enabled = isEnable.value
                )

                OutlinedTextField(
                    value = user.dob,
                    onValueChange = { user.dob = it},
                    label = { Text(text = "Enter dob")},
                    enabled = isEnable.value
                )

                OutlinedTextField(
                    value = user.email,
                    onValueChange = { user.email=it},
                    label = { Text(text = "Enter Email")},
                    enabled = isEnable.value
                )

                OutlinedTextField(
                    value = user.gender,
                    onValueChange = { user.gender=it},
                    label = { Text(text = "Enter Gender")},
                    enabled = isEnable.value
                )

                OutlinedTextField(
                    value = user.address,
                    onValueChange = { user.address =it},
                    label = { Text(text = "Enter Address")},
                    enabled = isEnable.value
                )

                OutlinedTextField(
                    value = "${user.cellNumber}",
                    onValueChange = { user.cellNumber = it.toInt()},
                    label = { Text(text = "Enter PhoneNumber")},
                    enabled = isEnable.value
                )
                Button(onClick = {
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

@Composable
fun imagePicker(): Uri? {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    //Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
     //   if (imageUri != null) {
     //       Image(painter = rememberImagePainter(imageUri), contentDescription = null, modifier = Modifier.fillMaxSize())
     //   } else {
     //       Button(onClick = { launcher.launch("image/*") }) {
     //           Text(text = "Pick Image")
     //       }
     //   }
     // }
    if (imageUri != null) {
        //Toast.makeText(LocalContext.current, "Image Selected", Toast.LENGTH_SHORT).show()
              Image(painter = rememberImagePainter(imageUri), contentDescription = null, modifier = Modifier.fillMaxSize())
              } else {
               Button(onClick = { launcher.launch("image/*") }) {
                Text(text = "Pick Image")
               }
          }
    //launcher.launch("image/*")
    return  imageUri

}
@Composable
fun OtherComposable(launcher: ActivityResultLauncher<String>) {
    Button(
        onClick = {
            launcher.launch("image/*")
        }
    ){

    }
}