package com.example.frametechapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frame_tech_app.Pages.Login
import com.example.frame_tech_app.Pages.Registration
import com.example.frametechapp.Controller.NetworkManager
import com.example.frametechapp.Controller.SessionManager
import com.example.frametechapp.Controller.SessionState
import com.example.frametechapp.Controller.SessionViewModel
import com.example.frametechapp.Controller.UserAccessClass
import com.example.frametechapp.Pages.About
import com.example.frametechapp.Pages.Cart
import com.example.frametechapp.Pages.CheckoutScreen
import com.example.frametechapp.Pages.Homepage
import com.example.frametechapp.Pages.Logout
import com.example.frametechapp.Pages.PaymentScreen
import com.example.frametechapp.Pages.ProductPage
import com.example.frametechapp.Pages.Profile
import com.example.frametechapp.Pages.SellingPage
import com.example.frametechapp.Pages.ServicePage
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val networkManager = NetworkManager()
        sessionManager = SessionManager(this)
        sessionViewModel = SessionViewModel(sessionManager, networkManager, this)

        setContent {
            AppNav(sessionViewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        // Start token refresh process when the app resumes
        lifecycleScope.launch {
            val storedToken = sessionManager.getToken()
            if (storedToken != null) {
                sessionViewModel.startTokenRefreshProcess(storedToken)
            } else {
                sessionViewModel.stopTokenRefreshProcess()
                navigateToLogin()  // Redirect to login if no token
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Stop token refresh process when the app is paused
        sessionViewModel.stopTokenRefreshProcess()
    }

    private fun navigateToLogin() {
        // Logic to navigate to the login screen
        sessionViewModel.logoutAndRedirect()
    }
}

@Composable
fun AppNav(sessionViewModel: SessionViewModel) {
    val navController = rememberNavController()
    val sessionState by sessionViewModel.sessionState.collectAsState()
    val verifyClass = UserAccessClass()

    LaunchedEffect(sessionState) {
        when (sessionState) {

            is SessionState.Error -> {
                // Handle error, e.g., show an error message and navigate to login
                // You might want to pass the error message to the login screen
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
                sessionViewModel.currentToken?.let { sessionViewModel.startTokenRefreshProcess(it) }

            }
            else ->{}
        }
    }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            Login(
                sessionViewModel = sessionViewModel,
                onLoginSuccess = { token ->
                    sessionViewModel.startTokenRefreshProcess(token)
                    navController.navigate("homeBase")
                },
                onCreate = { navController.navigate("registration") },
                onForgot = { navController.navigate("forgotPassword") }
            )
        }
        composable("registration") {
            Registration(
                sessionViewModel = sessionViewModel,
                onRegisterSuccess = { navController.navigate("login") }
            )
        }
        composable("forgotPassword") {
            verifyClass.ForgotPassword(navController = navController)
        }
        composable("homeBase") {
            HomeBase(sessionViewModel = sessionViewModel)
        }
    }
}

@Composable
fun HomeBase(sessionViewModel: SessionViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigateDrawer(navController = navController)
            }
        },
    ) {
        Scaffold(
            bottomBar = { TopNavBar(navController = navController) },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("MenuS") },
                    icon = { Icon(imageVector = Icons.Filled.Menu, contentDescription = null) },
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
        ) { padding ->
            Column {

                NavHost(navController = navController, startDestination = "homepage") {
                    composable("homepage") {
                        Homepage(sessionViewModel)
                    }
                    composable("cart") {
                        Cart(sessionViewModel,navController)
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
                        ServicePage(sessionViewModel)
                    }
                    composable("sellingPage") {
                        SellingPage(sessionViewModel)
                    }
                    composable("checkout"){
                        //CheckoutScreen(navController = navController)
                        PaymentScreen()

                    }
                    composable("logout") {
                        Logout(
                            sessionViewModel = sessionViewModel,
                            onLogout = {
                                sessionViewModel.stopTokenRefreshProcess()
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(padding))
            }
        }
    }
}