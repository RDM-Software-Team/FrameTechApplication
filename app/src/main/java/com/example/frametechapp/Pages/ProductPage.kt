package com.example.frametechapp.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frametech_app.Data.Category

@Preview(showBackground = true)
@Composable
fun ProductPage(){
 Column {
  //Text(text = "ProductPage")
  MainFrame()
 }
}

@Composable
fun MainFrame(){
//    val searchValue = remember { mutableStateOf("") }
//    val get = CategoryFilter()
// Column(
//  modifier = Modifier
//      .verticalScroll(rememberScrollState(), enabled = true)
//      .background(Color.LightGray)
//      .size(1200.dp, 4500.dp)
//      .padding(horizontal = 8.dp)
// ) {
//  //top options to search and filter
//
//  Row(
//   modifier = Modifier.width(900.dp),
//   verticalAlignment = Alignment.CenterVertically,
//   horizontalArrangement = Arrangement.SpaceBetween
//  ){
//   TextField(
//    value = searchValue.value,
//    onValueChange = { searchValue.value = it },
//    leadingIcon = {
//     IconButton(onClick = {}) {
//      Icon(Icons.Filled.Search, contentDescription = "Icon")//Need to make a working search function/action..
//     }
//    },
//    maxLines = 2,
//    modifier = Modifier
//        .width(220.dp)
//        .height(50.dp)
//   )
//
//   Button(onClick = { /*TODO*/ },
//    ) {
//    Icon(imageVector = Icons.AutoMirrored.Filled.Sort, contentDescription = null)
//    Text(text = "Filter")
//   }
//  }
//     HorizontalDivider(
//         Modifier
//             .background(Color.Black)
//             .padding(2.dp)
//     )
//  //Bottom view to display the items..
//   ProductView()
// }
}
@Composable
fun ProductView() {
//    val context = LocalContext.current
//    val get = CategoryFilter()
//
//    val groupLists = remember {
//        mutableStateOf(listOf<List<items>>(get.favList,get.newItemList,get.unBroghtItems))
//    }//This collects all List<items> in the system.
//
//
//    Column {
//        groupLists.value.forEach { itemList ->
//            Text(
//                text = itemList.firstOrNull()?.itemCategory ?: "New ",
//                style = TextStyle(fontWeight = FontWeight.Bold),
//                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//            )
//            LazyRow(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                items(itemList) { product ->
//                    Column(
//                        modifier = Modifier
//                            .padding(5.dp)
//                            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
//                            .clip(RoundedCornerShape(12.dp)),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.Start
//                    ) {
//                        Image(
//                            painter = painterResource(R.drawable.dell_latitude_5430),
//                            contentDescription = product.itemName,
//                            modifier = Modifier.width(290.dp)
//                        )
//                        Text(
//                            text = product.itemName,
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight(700),
//                        )
//                        Text(text = "R. ${product.itemPrice}")
//                        OutlinedButton(
//                            onClick = { /*TODO*/ },
//                            shape = RectangleShape,
//                            modifier = Modifier
//                                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
//                                .width(290.dp),
//                            colors = ButtonDefaults.outlinedButtonColors(
//                                Color.Magenta,
//                                contentColor = Color.White
//                            )
//                        ) {
//                            Text(text = "View")
//                        }
//                    }
//                    Spacer(modifier = Modifier.padding(10.dp))
//                }
//            }
//        }
//    }//This is the improved but not the final version which displays the product in a row but not based by their category
}

//fun exampleOfSearchCode(){
// val context = LocalContext.current
// val categories by sessionViewModel.categories.collectAsState()
// val products by sessionViewModel.products.collectAsState()
// val isLoading by sessionViewModel.isLoading
// val error by sessionViewModel.error.collectAsState()
//
// var selectedCategory by remember { mutableStateOf<Category?>(null) }
// var expandedProductId by remember { mutableStateOf<Int?>(null) }
// var pressExpended by remember { mutableStateOf(false) }
//
// LaunchedEffect(Unit) {
//  sessionViewModel.fetchCategories()
// }
//
// Column(
//  modifier = Modifier
//   .fillMaxSize()
//   .background(Color.LightGray)
// ) {
//  // Error handling
//  error?.let {
//   Text(text = it, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
//  }
//
//  // Loading state
//  if (isLoading) {
//   CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//  } else {
//   // Category Selection Dropdown
//   if (categories.isNotEmpty()) {
//    ExposedDropdownMenuBox(
//     expanded = pressExpended,
//     onExpandedChange = { pressExpended = it }
//    ) {
//     TextField(
//      value = selectedCategory?.category ?: "Select a Category",
//      onValueChange = {},
//      readOnly = true,
//      trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = pressExpended) },
//      modifier = Modifier.menuAnchor()
//     )
//
//     ExposedDropdownMenu(
//      expanded = pressExpended,
//      onDismissRequest = { pressExpended = false }
//     ) {
//      categories.forEach { category ->
//       DropdownMenuItem(
//        text = { Text(category.category) },
//        onClick = {
//         selectedCategory = category
//         pressExpended = false // Close the dropdown after selection
//         sessionViewModel.fetchProducts(category.category) // Fetch products for the new category
//        }
//       )
//      }
//     }
//    }
//
//    // Display products for the selected category
//    LazyColumn(
//     modifier = Modifier.weight(1f),
//     verticalArrangement = Arrangement.spacedBy(10.dp),
//     contentPadding = PaddingValues(vertical = 10.dp)
//    ) {
//     items(products) { product ->
//      if (expandedProductId == product.productId) {
//       MaxCard(
//        product = product,
//        onClick = { /* Handle add to cart */ },
//        onCancel = { expandedProductId = null }
//       )
//      } else {
//       MiniCard(
//        product = product,
//        onClick = { expandedProductId = product.productId }
//       )
//      }
//     }
//    }
//   } else {
//    Text(text = "No categories available", style = MaterialTheme.typography.bodyMedium)
//   }
//  }
// }
//}