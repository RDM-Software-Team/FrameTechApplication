package com.example.frametechapp.Pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.frametechapp.Controller.CategoryFilter
import com.example.frametechapp.Controller.NetworkManager
import com.example.frametechapp.Controller.SessionViewModel
import com.example.frametechapp.R


@Composable
fun Homepage(sessionViewModel: SessionViewModel) {
    val context = LocalContext.current
    val categories by sessionViewModel.categories.collectAsState(emptyList())
    val isExpanded = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        sessionViewModel.fetchProducts { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .background(Color.LightGray)
            .verticalScroll(rememberScrollState())
    ) {
        categories.forEach { category ->
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 10.dp)
            ) {
                items(category.items) { item ->
                    if (isExpanded.value) {
                        MaxCard(
                            itemId = item.productId,
                            itemsName = item.pName,
                            itemDescription = listOf(item.description), // Assuming descriptions are single-line for simplicity
                            itemsPrice = item.price,
                            itemsImage = listOf(R.drawable.default_image), // Placeholder if images are not provided
                            onClick = { /* Handle click */ },
                            onCancel = { /* Handle cancel */ }
                        )
                    } else {
                        MiniCard(
                            itemId = item.productId,
                            itemsName = item.pName,
                            itemsPrice = item.price,
                            itemsImage = listOf(R.drawable.default_image), // Placeholder if images are not provided
                            onClick = { /* Handle click */ }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun MiniCard(
    itemId: Int,
    itemsName: String,
    itemsPrice: Double,
    itemsImage: List<Int>,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val image = painterResource(itemsImage.firstOrNull() ?: R.drawable.default_image)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Black)
            .clip(RectangleShape)
            .clickable {
                onClick()
                Toast.makeText(context, "Card clicked: $itemsName", Toast.LENGTH_SHORT).show()
            }
            .padding(6.dp)
    ) {
        Image(painter = image, contentDescription = itemsName, modifier = Modifier.fillMaxWidth())
        Text(text = itemsName, style = MaterialTheme.typography.bodySmall)
        Text(text = "R $itemsPrice", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun MaxCard(
    itemId: Int,
    itemsName: String,
    itemDescription: List<String>,
    itemsPrice: Double,
    itemsImage: List<Int>,
    onClick: () -> Unit,
    onCancel: () -> Unit
) {
    val images = itemsImage.takeIf { it.isNotEmpty() } ?: listOf(R.drawable.default_image)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(8.dp)
    ) {
        Row {
            Sliding(images)
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = itemsName, style = MaterialTheme.typography.titleSmall)
                Text(text = itemDescription.getOrNull(0) ?: "", style = MaterialTheme.typography.bodyMedium)
                Text(text = "R $itemsPrice", style = MaterialTheme.typography.bodyMedium)
                Button(onClick = onClick) {
                    Text(text = "Add to Cart")
                }
            }
        }
    }
}

@Composable
fun Sliding(images: List<Int>) {
    var currentIndex by remember { mutableStateOf(0) }

    Row(modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = { if (currentIndex > 0) currentIndex-- }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
        }
        Image(
            painter = rememberAsyncImagePainter( images[currentIndex]),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        )
        IconButton(onClick = { if (currentIndex < images.size - 1) currentIndex++ }) {
            Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = null)
        }
    }
}
