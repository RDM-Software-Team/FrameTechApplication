package com.example.frame_tech_app.Pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frametechapp.Controller.SessionViewModel
import com.example.frametechapp.R

@Composable
fun Login(sessionViewModel: SessionViewModel,
          onLoginSuccess: () -> Unit,
          onForgot: ()-> Unit,
          onCreate: () ->Unit
          ){
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isLoading by sessionViewModel.isLoading

    Column(
        modifier = Modifier
            .border(1.dp, Color.Black)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Computer Complex", modifier = Modifier.padding(top = 20.dp))
        Row(
            modifier = Modifier.border(2.dp, Color.LightGray),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(R.drawable.default_person), contentDescription = null,modifier= Modifier.width(50.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text(text = "enter email/username") }
            )//Email||Username
        }

        Spacer(modifier = Modifier.padding(15.dp))
        Row(
            modifier = Modifier.border(2.dp, Color.LightGray),
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
                placeholder = { Text(text = "enter password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.padding(start = 12.dp)
            )//password
        }
        AnimatedVisibility(
            visible = !isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ){

            OutlinedButton(onClick = {
                sessionViewModel.login(email, password, onLoginSuccess) { error ->
                    errorMessage = error
                }
                //navController.navigate("homeBase")
            },
                shape = RectangleShape,
                border = BorderStroke(0.dp, Color.Transparent),
                colors = ButtonColors(containerColor = Color.Yellow, contentColor = Color.Black, disabledContentColor = Color.Black, disabledContainerColor = Color.LightGray),
                modifier = Modifier.padding(top = 15.dp)
            ) {
                Text(text = "Login")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Button(
                onClick = {
                    onForgot()
                    //navController.navigate("forgotPassword")
                },
                shape = RectangleShape,
                colors = ButtonColors(containerColor = Color.Gray, contentColor = Color.LightGray , disabledContentColor = Color.Transparent, disabledContainerColor = Color.Transparent),
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
                onCreate()
                //navController.navigate("registration")
            },
                colors = ButtonColors(containerColor = Color.Transparent, contentColor = Color.LightGray, disabledContentColor = Color.Black, disabledContainerColor = Color.Transparent)
                , modifier = Modifier
                    .width(150.dp)
                    .height(40.dp)
            ) {
                Text(text = "Create account", textDecoration = TextDecoration.Underline)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CircularProgressIndicator()
        }

    }
}
