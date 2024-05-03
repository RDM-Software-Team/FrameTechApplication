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
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingCart
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
    var isSelected by remember {//Will be used to keep track which button is selected
        mutableStateOf(false)
    }
    NavigationBar {
        NavigationBarItem(
            selected = isSelected,
            label = { Text(text = "homepage")},
            onClick = {
                navController.navigate("homepage")
                isSelected = true
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null)
            }
        )

        NavigationBarItem(
            selected = isSelected,
            label = { Text(text = "Menu")},
            onClick = {
                isSelected = true
            },
            icon = {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = null)
            }
        )

        NavigationBarItem(
            selected = isSelected,
            label = { Text(text = "Cart")},
            onClick = {
                navController.navigate("cart")
                isSelected = true
            },
            icon = {
                Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = null)
            }
        )

        NavigationBarItem(
            selected = isSelected,
            label = { Text(text = "Profile")},
            onClick = {
                navController.navigate("profile")
                isSelected = true
            },
            icon = {
                Icon(imageVector = Icons.Filled.Person, contentDescription = null)
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
                .clickable { navController.navigate("homepage")},
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
                .clickable { navController.navigate("productPage")},
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
                .clickable { navController.navigate("servicePage")},
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
                .clickable { navController.navigate("about")},
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(imageVector = Icons.Filled.Newspaper, contentDescription = null,modifier = Modifier.size(45.dp))
            Text(text = "About Us ", color = Color.White, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
        }
        Spacer(modifier = Modifier.padding(10.dp))
    }

}