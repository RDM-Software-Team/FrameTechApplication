package com.example.frametechapp.Pages

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frame_tech_app.Data.CartItem
import com.example.frametech_app.Data.items
import com.example.frametechapp.Controller.CategoryFilter
import com.example.frametechapp.Controller.SessionViewModel

@Composable
fun Cart(sessionViewModel: SessionViewModel) {
    val cartItems by sessionViewModel.cartItems
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        sessionViewModel.resetCartPagination()
        sessionViewModel.fetchCartItems { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()

        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Cart")

        if (cartItems.isEmpty()) {
            Text(text = "Your cart is empty")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(cartItems) { item ->
                    CartItemRow(item, sessionViewModel)
                }

                // Add a button to load more items if available
                item {
                    if (sessionViewModel.morePagesAvailable.value) {
                        Button(
                            onClick = {
                                sessionViewModel.loadMoreCartItems { error ->
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            Text(text = "Load More Items")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, sessionViewModel: SessionViewModel) {
    val context = LocalContext.current
    var quantity by remember { mutableStateOf(item.quantity) }
    var showDialog by remember { mutableStateOf(false) } // State to manage the dialog visibility

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Delete Item") },
            text = { Text(text = "Are you sure you want to delete ${item.itemId}?") },
            confirmButton = {
                Button(
                    onClick = {
                        sessionViewModel.deleteCartItem(item.itemId) { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                        showDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)
//            .align(Alignment.CenterHorizontally),
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.itemId.toString(),
            modifier = Modifier
                .border(1.dp, Color.Black)
                .width(80.dp)
                .fillMaxHeight()
        )
        OutlinedTextField(
            value = quantity.toString(),
            onValueChange = { input ->
                val newValue = input.toIntOrNull() ?: quantity
                if (newValue > 0) {
                    quantity = newValue
                    sessionViewModel.updateCartItem(item.itemId, quantity) { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .width(50.dp),
            textStyle = TextStyle(fontSize = 12.sp)
        )
        Text(
            text = "R." + item.itemId.toString(),
            modifier = Modifier
                .border(1.dp, Color.Black)
                .width(80.dp)
                .fillMaxHeight()
        )
        Text(
            text = "R." + (item.productId * quantity).toString(),
            modifier = Modifier
                .border(1.dp, Color.Black)
                .width(80.dp)
                .fillMaxHeight()
        )
        IconButton(
            onClick = { showDialog = true }, // Show confirmation dialog
            modifier = Modifier
                .padding(5.dp)
                .width(80.dp)
                .height(30.dp),
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}


@SuppressLint("MutableCollectionMutableState")
@Composable
fun CartTable(cartItems: List<items>, onDelete: (Int) -> Unit) {
    val context = LocalContext.current
    val columnNames = listOf("Item Name", "Quantity", "Price", "SubTotal", "Delete")
    val itemQuantities = remember { mutableStateOf(cartItems.map { 1 }.toMutableList()) }
    val itemPrices = remember { mutableStateOf(cartItems.map { it.itemPrice }.toMutableList()) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header section of the table
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(columnNames) {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f, true)
                )
            }
        }

        // Row section of the table
        LazyColumn {
            if (cartItems.isEmpty()) {
                item {
                    Text(
                        text = "Cart is Empty",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(cartItems) { item ->
                    val itemIndex = cartItems.indexOf(item)
                    var quantity by remember { mutableIntStateOf(1) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(7.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.itemName,
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .width(80.dp)
                                .fillMaxHeight()
                        )
                        OutlinedTextField(
                            value = quantity.toString(),
                            onValueChange = { input ->
                                try {
                                    val newValue = input.toIntOrNull() ?: 1
                                    if (newValue > 0) {
                                        quantity = newValue
                                        itemQuantities.value[itemIndex] = newValue
                                    }
                                } catch (e: NumberFormatException) {
                                    // Handle invalid input here
                                    quantity = 1 // Default to 1 if input is invalid
                                }
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .width(50.dp),
                            textStyle = TextStyle(fontSize = 12.sp)
                        )
                        Text(
                            text = "R." + item.itemPrice.toString(),
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .width(80.dp)
                                .fillMaxHeight()
                        )
                        Text(
                            text = "R." + (item.itemPrice * quantity).toString(),
                            modifier = Modifier
                                .border(1.dp, Color.Black)
                                .width(80.dp)
                                .fillMaxHeight()
                        )
                        IconButton(
                            onClick = {
                                onDelete(item.itemId)
                                Toast.makeText(
                                    context,
                                    "${item.itemId} + ${item.itemName}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier
                                .padding(5.dp)
                                .width(80.dp)
                                .height(30.dp),
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                    }
                }
            }
        }

        val totalAmount = totalAmount(itemPrices.value, itemQuantities.value)
        CheckOut(cartItems, totalAmount)
    }
}

@Composable
fun totalAmount(prices: List<Double>, quantities: List<Int>): MutableState<Double> {
    val totalAmount = remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(prices, quantities) {
        var total = 0.0
        for (i in prices.indices) {
            total += prices[i] * quantities[i]
        }
        totalAmount.doubleValue = total
    }

    Text(
        text = "Total Amount: R.${totalAmount.doubleValue}",
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Yellow)
    )

    return totalAmount
}

@Composable
fun CheckOut(cartItems: List<items>, totalAmount: MutableState<Double>) {
    val context = LocalContext.current
    var openDialog by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = {
            openDialog = true
            // It must collect all items being bought and the total amount

            Toast.makeText(context, "Check Out", Toast.LENGTH_SHORT).show()
        },
        enabled = cartItems.isNotEmpty(),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp)
    ) {
        Text(text = "Check Out")
    }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog
                openDialog = false
            },
            title = { Text(text = "Check Out") },
            confirmButton = {
                Button(
                    onClick = {
                        // Dismiss the dialog
                        openDialog = false
                    }
                ) {
                    Text(text = "Buy")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        // Handle dismiss button click
                        openDialog = false
                    }
                ) {
                    Text(text = "Cancel")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp)
        )
    }
}
