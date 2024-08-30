package com.example.frametechapp.Controller

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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