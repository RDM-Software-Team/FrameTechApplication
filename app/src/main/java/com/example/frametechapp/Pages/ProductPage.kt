package com.example.frametechapp.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frametech_app.Data.Category
import com.example.frametechapp.R

@Preview(showBackground = true)
@Composable
fun ProductPage() {
 Column(
  modifier = Modifier
   .fillMaxSize()
   .padding(16.dp)
 ) {
  MainFrame()
 }
}

@Composable
fun MainFrame() {
 val searchValue = remember { mutableStateOf("") }
 val isFilterOpen = remember { mutableStateOf(false) }

 Column(
  modifier = Modifier
   .background(Color.White)
   .padding(horizontal = 16.dp)
 ) {
  // Top section for search and filter
  Row(
   modifier = Modifier
    .fillMaxWidth()
    .padding(vertical = 16.dp),
   verticalAlignment = Alignment.CenterVertically,
   horizontalArrangement = Arrangement.SpaceBetween
  ) {
   // Search field
   TextField(
    value = searchValue.value,
    onValueChange = { searchValue.value = it },
    placeholder = { Text("Search products...") },
    leadingIcon = {
     IconButton(onClick = { /* Handle Search */ }) {
      Icon(Icons.Filled.Search, contentDescription = "Search Icon")
     }
    },
    modifier = Modifier
     .fillMaxWidth(0.7f)
     .height(56.dp),
    shape = RoundedCornerShape(12.dp)
   )

   // Filter button
   Button(
    onClick = { isFilterOpen.value = !isFilterOpen.value },
    shape = RoundedCornerShape(12.dp),
    modifier = Modifier.height(56.dp)
   ) {
    Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Filter Icon")
    Spacer(modifier = Modifier.width(8.dp))
    Text(text = "Filter", color = Color.White)
   }
  }

  Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray)

  // Product view
  ProductView()
 }
}

@Composable
fun ProductView() {
 val products = listOf(
  // Example data
  Product("Dell Latitude", "15,000", R.drawable.dell_latitude_5430),
  Product("MacBook Pro", "25,000", R.drawable.default_image),
  Product("Lenovo ThinkPad", "12,000", R.drawable.logitech_wireless_desktop_mk330__keyboard__mouse_combo)
 )

 LazyVerticalGrid(
  columns = GridCells.Fixed(2),
  modifier = Modifier.fillMaxSize(),
  contentPadding = PaddingValues(16.dp),
  verticalArrangement = Arrangement.spacedBy(16.dp),
  horizontalArrangement = Arrangement.spacedBy(16.dp)
 ) {
  items(products) { product ->
   ProductCard(product)
  }
 }
}


@Composable
fun ProductCard(product: Product) {
 Card(
  shape = RoundedCornerShape(12.dp),
  modifier = Modifier
   .fillMaxWidth()
   .height(250.dp)
   .clickable { /* Handle click */ }
 ) {
  Column(
   modifier = Modifier
    .padding(16.dp)
    .fillMaxSize(),
   verticalArrangement = Arrangement.SpaceBetween,
   horizontalAlignment = Alignment.CenterHorizontally
  ) {
   Image(
    painter = painterResource(id = product.imageRes),
    contentDescription = product.name,
    modifier = Modifier
     .fillMaxWidth()
     .height(120.dp)
   )

   Text(
    text = product.name,
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold,
    modifier = Modifier.padding(top = 8.dp)
   )

   Text(
    text = "R. ${product.price}",
    fontSize = 14.sp,
    fontWeight = FontWeight.SemiBold,
    color = Color(0xFF6200EE),
    modifier = Modifier.padding(top = 4.dp)
   )

   OutlinedButton(
    onClick = { /* Handle view click */ },
    shape = RoundedCornerShape(8.dp),
    modifier = Modifier
     .fillMaxWidth()
     .height(40.dp),
//    colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
//     containerColor = Color(0xFF6200EE),
//     contentColor = Color(0xFF6200EE),
//     disabledContentColor = Color.White,
//     disabledContainerColor = Color(0xFF6200EE),
//     backgroundColor = Color(0xFF6200EE),
//     contentColor = Color.White,
//
//     )
   ) {
    Text(text = "View", fontSize = 14.sp)
   }
  }
 }
}

data class Product(val name: String, val price: String, val imageRes: Int)
