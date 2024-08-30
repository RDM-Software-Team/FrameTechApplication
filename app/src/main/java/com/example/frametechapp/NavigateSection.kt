package com.example.frametechapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TopNavBar(navController: NavController){
    var selectedRoute by remember { mutableStateOf("homepage") }

    NavigationBar {
        NavigationBarItem(
            selected = selectedRoute == "homepage",
            label = { Text(text = "Homepage") },
            onClick = {
                navController.navigate("homepage") {
                    // Ensure only one copy of the route is in the back stack
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                }
                selectedRoute = "homepage"
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null
                )
            }
        )

        NavigationBarItem(
            selected = selectedRoute == "menu",
            label = { Text(text = "Menu") },
            onClick = {
                navController.navigate("menu") {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                }
                selectedRoute = "menu"
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null
                )
            }
        )

        NavigationBarItem(
            selected = selectedRoute == "cart",
            label = { Text(text = "Cart") },
            onClick = {
                navController.navigate("cart") {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                }
                selectedRoute = "cart"
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = null
                )
            }
        )

        NavigationBarItem(
            selected = selectedRoute == "profile",
            label = { Text(text = "Profile") },
            onClick = {
                navController.navigate("profile") {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                }
                selectedRoute = "profile"
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            }
        )
    }
}

@Composable
fun NavigateDrawer(navController: NavController){
    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
                .clip(shape = RoundedCornerShape(15.dp))
                .clickable { navController.navigate("homepage") },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(imageVector = Icons.Filled.Home, contentDescription = null,modifier = Modifier.size(45.dp))
            Text(text = "Home", color = Color.White, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
                .clip(shape = RoundedCornerShape(15.dp))
                .clickable { navController.navigate("productPage") },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(imageVector = Icons.Filled.Shop, contentDescription = null,modifier = Modifier.size(45.dp))
            Text(text = "Products", color = Color.White, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
                .clip(shape = RoundedCornerShape(15.dp))
                .clickable { navController.navigate("servicePage") },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(imageVector = Icons.Filled.HomeRepairService, contentDescription = null,modifier = Modifier.size(45.dp))
            Text(text = "Service", color = Color.White, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
                .clip(shape = RoundedCornerShape(15.dp))
                .clickable { navController.navigate("about") },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(imageVector = Icons.Filled.Newspaper, contentDescription = null,modifier = Modifier.size(45.dp))
            Text(text = "About Us ", color = Color.White, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
                .clip(shape = RoundedCornerShape(15.dp))
                .clickable { navController.navigate("sellingPage") },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(imageVector = Icons.Filled.Sell, contentDescription = null,modifier = Modifier.size(45.dp))
            Text(text = "Sell Your Item ", color = Color.White, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
        }
        HorizontalDivider(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
                .clip(shape = RoundedCornerShape(15.dp))
                .clickable { navController.navigate("Logout") },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(imageVector = Icons.Filled.Sell, contentDescription = null,modifier = Modifier.size(45.dp))
            Text(text = "Logout ", color = Color.White, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
        }
    }

}