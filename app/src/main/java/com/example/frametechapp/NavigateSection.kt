package com.example.frametechapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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