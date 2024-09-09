@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.frametechapp.Pages

import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.frametech_app.Data.Product
import com.example.frametechapp.Controller.SessionViewModel


@Composable
fun Homepage(sessionViewModel: SessionViewModel) {
    val context = LocalContext.current
    val categories by sessionViewModel.categories.collectAsState()
    val products by sessionViewModel.products.collectAsState()
    val isLoading by sessionViewModel.isLoading
    val error by sessionViewModel.error.collectAsState()

    var expandedProductId by remember { mutableStateOf<Int?>(null) }
    val loadingMoreProducts = remember { mutableStateOf(false) } // Track loading more products

    // Fetch all categories when the composable is first launched
    LaunchedEffect(Unit) {
        sessionViewModel.fetchCategories()
        categories.forEach { category ->
            sessionViewModel.fetchProducts(category.category, refresh = true) // Fetch initial products for each category
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        // Error handling
        error?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
        }

        // Loading state
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            if (categories.isNotEmpty()) {
                // Group products by category and display them with pagination
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    categories.forEach { category ->
                        item {
                            // Category Header
                            Text(
                                text = category.category,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // Products under the current category
                        val categoryProducts = products.filter { it.category == category.category }
                        items(categoryProducts) { product ->
                            if (expandedProductId == product.productId) {
                                MaxCard(
                                    product = product,
                                    onClick = { sessionViewModel.addToCart(product.productId, 1, onSuccess = {
                                        Toast.makeText(context,"Added to the cart",Toast.LENGTH_SHORT).show()
                                    }, onError ={
                                        Toast.makeText(context,it,Toast.LENGTH_SHORT).show()

                                    }) },
                                    onCancel = { expandedProductId = null }
                                )
                            } else {
                                MiniCard(
                                    product = product,
                                    onClick = { expandedProductId = product.productId }
                                )
                            }
                        }

                        // Pagination: Load more when reaching the bottom
                        item {
                            if (categoryProducts.size >= 10) { // Arbitrary limit for batch size
                                if (loadingMoreProducts.value) {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                                } else {
                                    TextButton(onClick = {
                                        // Load more products when clicked
                                        loadingMoreProducts.value = true
                                        sessionViewModel.fetchProducts(category.category) // Trigger fetching the next page of products
                                        loadingMoreProducts.value = false
                                    }) {
                                        Text("Load More")
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Text(text = "No categories available", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


@Composable
fun MiniCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imagePath,
                contentDescription = product.pName,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = product.pName, style = MaterialTheme.typography.titleMedium)
                Text(text = "R ${product.price}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun MaxCard(product: Product, onClick: () -> Unit, onCancel: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = product.imagePath,
                contentDescription = product.pName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = product.pName, style = MaterialTheme.typography.titleLarge)
            Text(text = product.description, style = MaterialTheme.typography.bodyMedium)
            Text(text = "R ${product.price}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onClick) {
                    Text("Add to Cart")
                }
                OutlinedButton(onClick = onCancel) {
                    Text("Cancel")
                }
            }
        }
    }
}
