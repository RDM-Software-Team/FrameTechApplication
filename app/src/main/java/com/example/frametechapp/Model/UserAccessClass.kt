package com.example.frametechapp.Model

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frametechapp.R

class UserAccessClass {
    @Composable
    fun Login(navController: NavController){
        var username by remember {
            mutableStateOf("")
        }
        var password by remember {
            mutableStateOf("")
        }

        Column(
            modifier = Modifier
                .border(1.dp, Color.Black)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Computer Complex", modifier = Modifier.padding(top = 20.dp))
            Row(
                modifier = Modifier.border(2.dp,Color.LightGray),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(R.drawable.default_person), contentDescription = null,modifier=Modifier.width(50.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text(text = "enter email/username")}
                    )//Email||Username
            }
            Spacer(modifier = Modifier.padding(15.dp))
            Row(
                modifier = Modifier.border(2.dp,Color.LightGray),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.default_password),
                    contentDescription = null,

                    modifier= Modifier
                        .width(38.dp)
                        .padding(start = 10.dp)
                    )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text(text = "enter password")},
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.padding(start = 12.dp)
                )//password
            }
            OutlinedButton(onClick = {
                                     navController.navigate("homeBase")
            },
                    shape = RectangleShape,
                    border = BorderStroke(0.dp,Color.Transparent),
                    colors = ButtonColors(containerColor = Color.Yellow, contentColor = Color.Black, disabledContentColor = Color.Black, disabledContainerColor = Color.LightGray),
                    modifier = Modifier.padding(top = 15.dp)
                ) {
                Text(text = "Login")
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Button(
                    onClick = {
                              navController.navigate("forgotPassword")
                    },
                    shape = RectangleShape,
                    colors = ButtonColors(containerColor = Color.Gray, contentColor =Color.LightGray , disabledContentColor = Color.Transparent, disabledContainerColor = Color.Transparent),
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .border(0.dp, Color.Transparent)
                ) {
                    Text(text = "Forgot password?", textDecoration = TextDecoration.Underline)
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(10.dp)
                    .background(Color.Gray)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Don't have an account: ", color = Color.Gray)
                Button(onClick = {
                                 navController.navigate("registration")
                },
                    colors = ButtonColors(containerColor = Color.Transparent, contentColor = Color.LightGray, disabledContentColor = Color.Black, disabledContainerColor = Color.Transparent)
                 , modifier = Modifier
                        .width(150.dp)
                        .height(40.dp)
                ) {
                    Text(text = "Create account", textDecoration = TextDecoration.Underline)
                }
            }
        }
    }

    @Composable
    fun Registration(navController: NavController){
        val firstname = remember { mutableStateOf("") }
        val lastname = remember  { mutableStateOf("") }
        val email    = remember  { mutableStateOf("") }
        val cellNumber = remember { mutableStateOf("") }
        val password = remember  { mutableStateOf("") }
        val confirmPassword = remember  { mutableStateOf("") }
        val onChange = remember {
            mutableStateOf(false)
        }
        val match = remember {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            
        ) {
            Text(text = "Computer Complex", textAlign = TextAlign.Center,modifier = Modifier
                .fillMaxWidth()
                .height(20.dp))
            Row(
                modifier = Modifier.border(2.dp, Color.LightGray),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.default_person),
                    contentDescription = null,
                    modifier=Modifier.width(50.dp)
                )
                OutlinedTextField(
                    value = firstname.value,
                    onValueChange = { firstname.value = it },
                    placeholder = { Text(text = "enter firstname")}
                )
            }
            Spacer(modifier = Modifier.padding(20.dp))
            Row(
                modifier = Modifier.border(2.dp, Color.LightGray),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.default_person),
                    contentDescription = null,
                    modifier=Modifier.width(50.dp)
                )
                OutlinedTextField(
                    value = lastname.value,
                    onValueChange = { lastname.value=it },
                    placeholder = { Text(text = "enter lastname")}
                )
            }
            Spacer(modifier = Modifier.padding(20.dp))
            Row(
                modifier = Modifier.border(2.dp, Color.LightGray),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(painter = painterResource(R.drawable.default_email), 
                    contentDescription = null,
                    modifier=Modifier.width(50.dp)
                )
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value=it },
                    placeholder = { Text(text = "enter email")}
                )
            }
            Spacer(modifier = Modifier.padding(20.dp))
            Row(
                modifier = Modifier.border(2.dp, Color.LightGray),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.default_numbers),
                    contentDescription = null,
                    modifier=Modifier.width(50.dp)
                )
                OutlinedTextField(
                    value = cellNumber.value,
                    onValueChange = { cellNumber.value=it },
                    placeholder = { Text(text = "enter cellNumber")}
                )
            }
            Spacer(modifier = Modifier.padding(20.dp))
            Row(
                modifier = Modifier.border(2.dp, Color.LightGray),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.default_password),
                    contentDescription = null,
                    modifier=Modifier.width(50.dp)
                )
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value=it 
                        //VerifyPassword(password = password.value)
                        onChange.value = true
                                    },
                    visualTransformation = PasswordVisualTransformation(),
                    placeholder = { Text(text = "enter password")}
                    )
            }
            if(onChange.value){
                VerifyPassword(password = password.value)
            }
            Spacer(modifier = Modifier.padding(20.dp))
            Row(
                modifier = Modifier.border(2.dp, if(match.value) Color.LightGray else Color.Red),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.default_password),
                    contentDescription = null,
                    modifier=Modifier.width(50.dp)
                )
                OutlinedTextField(
                    value = confirmPassword.value,
                    onValueChange = { confirmPassword.value=it
                                    if(password.value == confirmPassword.value){
                                        match.value = true
                                    }
                                    },
                    placeholder = { Text(text = "confirm password")},
                    visualTransformation = PasswordVisualTransformation(),
                    )
            }
            Spacer(modifier = Modifier.padding(20.dp))
            OutlinedButton(onClick = { navController.navigate("login") }) {

                Text(text = "Register")
            }
        }
    }
    @Composable
    fun ForgotPassword(navController: NavController){

        val verifyEmail = remember {
            mutableStateOf("")
        }
        var newPassword = remember {
            mutableStateOf("")
        }
        var confirmNewPassword = remember {
            mutableStateOf("")
        }
        var isVisable = remember {//Will be used to display the form for the new password input
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.border(2.dp, Color.LightGray),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.default_email),
                    contentDescription = null,
                    modifier = Modifier.width(50.dp)
                )
                OutlinedTextField(
                    value = verifyEmail.value,
                    onValueChange = { verifyEmail.value = it },
                    placeholder = { Text(text = "Enter email")}
                )//Confirm email
            }
            Button(onClick = { /*TODO*/ },
                shape = RectangleShape
                ) {
                Text(text = "Confirm ")
            }
        }
    }

    fun verifyEmail(email:String){//Will be used in the forgot password

    }
    @Composable
    fun VerifyPassword(password:String){
        val count = 9
        val specialChar = arrayOf('!','@','#','$','%','^','&','*','(',')','`','~')
        val upperCase = arrayOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z')
        val lowerCase = arrayOf('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z')
        val digits = arrayOf('0','1','2','3','4','5','6','7','8','9')
        val conditions = listOf(
            "Password must be 9 characters long",
            "Password must contain an uppercase letter",
            "Password must contain a lowercase letter",
            "Password must contain a special character",
            "Password must contain a number"
        )

        var isSpecialChar = false
        var isUppercase = false
        var isDigits = false
        var isLower = false

        if(password.isNotEmpty()){
            for (upper in upperCase){
                if (password.contains(upper)) {
                    isUppercase = true
                    break
                }
            }
            for (lower in lowerCase){
                if (password.contains(lower)) {
                    isLower = true
                    break
                }
            }
            for (special in specialChar){
                if (password.contains(special)) {
                    isSpecialChar = true
                    break
                }
            }
            for (nums in digits){
                if (password.contains(nums)) {
                    isDigits = true
                    break
                }
            }
            // Checking conditions
            val isValidPassword = password.length >= count && isUppercase && isLower && isSpecialChar && isDigits

            // Display UI
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                conditions.forEachIndexed { index, condition ->
                    Text(
                        text = condition,
                        color = if (index == 0 && password.length < count || (index == 1 && !isUppercase) ||
                            (index == 2 && !isLower) || (index == 3 && !isSpecialChar) || (index == 4 && !isDigits))
                            Color.Red
                        else
                            Color.Green
                    )
                }
                Text(
                    text = "Password is ${if (isValidPassword) "valid" else "invalid"}",
                    color = if (isValidPassword) Color.Green else Color.Red
                )
            }
        }
    }

}