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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
@Preview(showBackground = true)
@Composable
fun Profile() {
    ProfileFrame(userProfile)
}

@Composable
fun ProfileFrame(profile: List<profileData>) {
    val context = LocalContext.current
    val isEditMode = remember { mutableStateOf(false) }
    var firstname by remember { mutableStateOf(profile[0].firstname) }
    var lastname by remember { mutableStateOf(profile[0].lastname) }
    var dob by remember { mutableStateOf(profile[0].dob) }
    var email by remember { mutableStateOf(profile[0].email) }
    var gender by remember { mutableStateOf(profile[0].gender) }
    var address by remember { mutableStateOf(profile[0].address) }
    var cellNumber by remember { mutableStateOf(profile[0].cellNumber) }

    // Validation states
    var isValid by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    val currentProfile = remember { mutableStateOf(profile) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Profile Picture
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = if (imageUri != null) rememberImagePainter(imageUri) else painterResource(id = R.drawable.default_person),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape)
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
            Column {
                Text(text = firstname + " " + lastname, style = MaterialTheme.typography.titleSmall)
                Text(text = email, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = {
                isEditMode.value = !isEditMode.value
                Toast.makeText(context, if (isEditMode.value) "Edit Mode" else "View Mode", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    painter = painterResource(id = if (isEditMode.value) R.drawable.save_icon else R.drawable.edit_pen),
                    contentDescription = null
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            color = Color.Gray,
            thickness = 1.dp
        )

        // Profile Form with Validation
        ProfileTextField("First Name", firstname, isEditMode.value) { firstname = it }
        ProfileTextField("Last Name", lastname, isEditMode.value) { lastname = it }
        ProfileTextField("Date of Birth", dob, isEditMode.value) { dob = it }
        ProfileTextField("Email", email, isEditMode.value, error = !isValid && !isValidEmail(email)) { email = it }
        ProfileTextField("Gender", gender, isEditMode.value) { gender = it }
        ProfileTextField("Address", address, isEditMode.value) { address = it }
        ProfileTextField("Phone Number", cellNumber, isEditMode.value, error = !isValid && !isValidPhoneNumber(cellNumber)) { cellNumber = it }

        if (!isValid) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isEditMode.value) {
            Button(
                onClick = {
                    // Validate inputs before saving
                    if (validateProfileData(firstname, lastname, email, cellNumber)) {
                        val updatedProfile = updateUserProfile(
                            profile[0].customerId, firstname, lastname, cellNumber, dob, imageUri, email, address, gender
                        )
                        currentProfile.value = updatedProfile
                        isEditMode.value = false
                        Toast.makeText(context, "Profile Saved", Toast.LENGTH_SHORT).show()
                    } else {
                        isValid = false
                        errorMessage = "Please ensure all fields are valid."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue)
            ) {
                Text(text = "Save", color = Color.White)
            }
        }

        // Membership Details and Profile Statistics
        ProfileDetailsSection()
    }
}

// Input validation
fun validateProfileData(firstname: String, lastname: String, email: String, phoneNumber: String): Boolean {
    return firstname.isNotBlank() && lastname.isNotBlank() && isValidEmail(email) && isValidPhoneNumber(phoneNumber)
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return email.matches(emailRegex)
}

fun isValidPhoneNumber(phoneNumber: String): Boolean {
    return phoneNumber.length == 10 && phoneNumber.all { it.isDigit() }
}

@Composable
fun ProfileDetailsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Membership Details", style = MaterialTheme.typography.titleSmall)
        Text(text = "Member since: 12/01/2020")
        Text(text = "Membership level: Gold")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Profile Statistics", style = MaterialTheme.typography.titleSmall)
        Text(text = "Total Orders: 35")
        Text(text = "Total Money Spent: $2,500")
        Text(text = "Recent Activities: 5 new orders in the past month")
    }
}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    enabled: Boolean,
    error: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        isError = error,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        /**colors = androidx.compose.material3.TextFieldDefaults(
            disabledBorderColor = Color.Gray,
            disabledTextColor = Color.Gray
        )**/
    )
}

fun updateUserProfile(
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