package com.example.frametechapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frame_tech_app.Pages.Cart
import com.example.frame_tech_app.Pages.Homepage
import com.example.frametechapp.Pages.Profile
import com.example.frametechapp.ui.theme.FrameTechAppTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.frame_tech_app.Pages.About
import com.example.frame_tech_app.Pages.ProductPage
import com.example.frame_tech_app.Pages.ServicePage
import kotlinx.coroutines.launch


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBase(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigateDrawer(navController = navController)
            }
        },
    ){
        Scaffold(
            bottomBar = { TopNavBar(navController = navController)},
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Menu") },
                    icon = {  Icon(imageVector = Icons.Filled.Menu, contentDescription = null) },
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
            ) {padding ->
            Column {
                NavHost(navController = navController, startDestination = "homepage") {
                    composable("homepage") {
                        Homepage()
                    }
                    composable("cart") {
                        Cart()
                    }
                    composable("profile") {
                        Profile()
                    }
                    composable("productPage") {
                        ProductPage()
                    }
                    composable("about") {
                        About()
                    }
                    composable("servicePage") {
                        ServicePage()
                    }
                }
                Spacer(modifier = Modifier.padding(padding))
            }
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