package com.example.frametechapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frame_tech_app.Pages.Cart
import com.example.frame_tech_app.Pages.Homepage
import com.example.frametechapp.Pages.Profile
import com.example.frametechapp.ui.theme.FrameTechAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrameTechAppTheme {
                HomeBase()
            }
        }
    }
}

@Composable
fun HomeBase(){
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { TopNavBar(navController = navController)}
    ) {padding ->
        Column {
            NavHost(navController = navController, startDestination = "homepage") {
                composable("homepage") {
                    Homepage()
                }
                composable("homepage") {
                    Homepage()
                }
                composable("cart") {
                    Cart()
                }
                composable("profile") {
                    Profile()
                }
            }
            Spacer(modifier = Modifier.padding(padding))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    FrameTechAppTheme {
        HomeBase()
    }
}