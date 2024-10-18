@file:Suppress("UNREACHABLE_CODE", "PackageName")

package com.example.frametechapp.Pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.frame_tech_app.Data.CartItem
import com.example.frametechapp.Controller.PaymentMethod
import com.example.frametechapp.Controller.PaymentViewModel
import com.example.frametechapp.Controller.SessionViewModel

@Composable
fun Cart(sessionViewModel: SessionViewModel, navController: NavController) {
    //val cartItems by sessionViewModel.cartItems
    val context = LocalContext.current
    // Collect cartItems and cartMessage as StateFlows
    val cartItems by sessionViewModel.cartItems.collectAsState(emptyList())  // Default to empty list
    val cartMessage by sessionViewModel.cartMessage.collectAsState(null)    // Null if no message
    val orderMessage by sessionViewModel.orderMessage.collectAsState(null)

    // Show the cart message as a Toast whenever it is not null
    cartMessage?.let {
        LaunchedEffect(it) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            sessionViewModel.clearCartMessage()  // Optionally clear the message after showing it
        }
    }

    orderMessage?.let {
        LaunchedEffect(it) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        sessionViewModel.resetCartPagination()
        sessionViewModel.fetchCartItems { error ->
            sessionViewModel.setCartMessage(error) // Set the error message to be displayed
        }
    }

    LaunchedEffect(Unit) {
        sessionViewModel.resetCartPagination()
        sessionViewModel.fetchCartItems { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Cart", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 16.dp))

        if (cartItems.isEmpty()) {
            Text(text = "Your cart is empty", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems) { item ->
                    CartItemRow(item, sessionViewModel)
                }

                item {
                    if (sessionViewModel.morePagesAvailable.value) {
                        Button(
                            onClick = {
                                sessionViewModel.loadMoreCartItems { error ->
                                    sessionViewModel.setCartMessage(error)  // Set error message if load fails
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = "Load More Items")
                        }
                    }
                }
            }

            // Subtotal and Total Amount
            val subtotal = cartItems.sumOf { it.productPrice * it.quantity }
            val totalAmount = subtotal + (subtotal * 0.15)  // Assuming 15% tax for demonstration

            Text(
                text = "Subtotal: R.%.2f".format(subtotal),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "Total: R.%.2f".format(totalAmount),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Checkout and navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("homepage") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text(text = "Continue Shopping")
                }

                Button(
                    onClick = { //navController.navigate("checkout")
                        sessionViewModel.createOrder(onSuccess = { orderId, totalPrice ->
                            // Successfully created the order, navigate to the payment screen
                            ///navController.navigate("payment_screen/$orderId/$totalPrice")
                            navController.navigate("checkout/$orderId/$totalPrice")
                        }, onError = { errorMessage ->
                            // Show error message if order creation fails
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        })
                              },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Checkout")
                }
            }


        }
    }
}

@Composable
fun CartItemRow(item: CartItem, sessionViewModel: SessionViewModel) {
    val context = LocalContext.current
    var quantity by remember { mutableStateOf(item.quantity) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Delete Item") },
            text = { Text(text = "Are you sure you want to delete ${item.itemId}?") },
            confirmButton = {
                Button(
                    onClick = {
                        sessionViewModel.deleteCartItem(item.itemId)
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
            .padding(8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(model = item.productImage, contentDescription = item.productName, modifier = Modifier.size(64.dp))
        Text(text = item.productName, modifier = Modifier.weight(1f))

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
                if(newValue == 0){
                    sessionViewModel.deleteCartItem(item.itemId)
                }
            },
            modifier = Modifier.width(60.dp),
            textStyle = TextStyle(fontSize = 14.sp)
        )

        Text(text = "R.${item.productPrice}", modifier = Modifier.width(80.dp))

        Text(text = "R.${(item.productPrice * quantity)}", modifier = Modifier.width(80.dp))

        IconButton(onClick = { showDialog = true }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}

@Composable
fun CheckoutScreen(navController: NavController) {
    val paymentOptions = listOf("Credit Card", "Debit Card", "PayPal", "Google Pay", "Bank Transfer")
    var selectedOption by remember { mutableStateOf(paymentOptions[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Choose a Payment Option", style = MaterialTheme.typography.titleSmall)

        // Show list of payment options
        paymentOptions.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { selectedOption = option }
            ) {
                RadioButton(
                    selected = (selectedOption == option),
                    onClick = { selectedOption = option }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option)
            }
        }

        // Handle selected payment option
//        when (selectedOption) {
//            "Credit Card", "Debit Card" -> CreditDebitCardPayment(Stripe(LocalContext.current, "YOUR_PUBLISHABLE_KEY"))
//            "PayPal" -> PayPalPayment()
//            "Google Pay" -> GooglePayPayment()
//            "Bank Transfer" -> BankTransferPayment()
//        }
    }
        OutlinedButton(
            onClick = { navController.popBackStack() }, // Go back to Cart
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Go Back to Cart")
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(orderId: String, totalPrice: String,sessionViewModel: SessionViewModel,navController: NavController) {
    val context = LocalContext.current
    val paymentMethods by sessionViewModel.paymentMethods.collectAsState()

    var amount by remember { mutableStateOf(totalPrice) }
    var currency by remember { mutableStateOf("USD") }
    var selectedPaymentMethod by remember { mutableStateOf(paymentMethods.firstOrNull()?.name ?: "") }
    var expanded by remember { mutableStateOf(false) }

    // List of currencies to choose from
    val currencyOptions = listOf("USD", "ZAR", "EUR", "GBP")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                "Payment Gateway Demo",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        item {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = currency,
                    onValueChange = { },
                    label = { Text("Currency") },
                    trailingIcon = {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    currencyOptions.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                currency = option
                                expanded = false
                            },
                            text = { Text(option) },
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        item {
            Text("Select Payment Method")
        }

        items(paymentMethods) { method ->
            PaymentMethodItem(method) { selectedMethod ->
                selectedPaymentMethod = selectedMethod
            }
        }

        item {
            Button(
                onClick = {
                    // Ensure a valid payment method is selected
                    if (selectedPaymentMethod.isNotEmpty()) {
                        sessionViewModel.processPayment(
                            orderId = orderId, // Create a unique order ID
                            paymentType = selectedPaymentMethod,
                            onSuccess = {
                                Toast.makeText(context, "Payment Successful!", Toast.LENGTH_SHORT).show()
                                navController.navigate("homepage")
                            },
                            onError = { errorMessage ->
                                Toast.makeText(context, "Payment Failed: $errorMessage", Toast.LENGTH_SHORT).show()
                            }
                        )
                    } else {
                        Toast.makeText(context, "Please select a payment method.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Pay Now")
            }
        }
    }
}

@Composable
fun PaymentMethodItem(method: PaymentMethod, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onSelect(method.name) }, // Call the onSelect callback to update selectedPaymentMethod
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = method.iconRes),
            contentDescription = method.name,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(method.name)
    }
    when(method.name){
        "Credit Card"->  CreditCardDetails()
        "Debit Card"-> DebitCardDetails()
        "PayPal"-> PayPalDetails()
        "Google Pay"-> GooglePayDetails()
        "Bank Transfer"-> BankTransferDetails()
    }
}

@Composable
fun CreditCardDetails() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        TextField(
            value = "",
            onValueChange = { /* Handle card number change */ },
            label = { Text("Card Number", color = Color(0xFF616161)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color(0xFF6200EE),
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color(0xFFC5CAE9)
            )
//            TextFieldDefaults.textFieldColors(
//                backgroundColor = Color.White,
//                cursorColor = Color(0xFF6200EE),
//                focusedIndicatorColor = Color(0xFF6200EE),
//                unfocusedIndicatorColor = Color(0xFFC5CAE9)
//            )
        )
        TextField(
            value = "",
            onValueChange = { /* Handle card expiry change */ },
            label = { Text("Expiry Date (MM/YY)", color = Color(0xFF616161)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color(0xFF6200EE),
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color(0xFFC5CAE9)
            )

        )
        TextField(
            value = "",
            onValueChange = { /* Handle CVV change */ },
            label = { Text("CVV", color = Color(0xFF616161)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color(0xFF6200EE),
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color(0xFFC5CAE9)
            )
//
        )
    }
}

@Composable
fun DebitCardDetails() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        TextField(
            value = "",
            onValueChange = { /* Handle debit card number change */ },
            label = { Text("Debit Card Number", color = Color(0xFF616161)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color(0xFF6200EE),
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color(0xFFC5CAE9)
            )
//
        )
        TextField(
            value = "",
            onValueChange = { /* Handle debit card expiry date change */ },
            label = { Text("Expiry Date (MM/YY)", color = Color(0xFF616161)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color(0xFF6200EE),
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color(0xFFC5CAE9)
            )
//
        )
        TextField(
            value = "",
            onValueChange = { /* Handle CVV change */ },
            label = { Text("CVV", color = Color(0xFF616161)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color(0xFF6200EE),
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color(0xFFC5CAE9)
            )
//
        )
    }
}

@Composable
fun PayPalDetails() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        TextField(
            value = "",
            onValueChange = { /* Handle PayPal email change */ },
            label = { Text("PayPal Email", color = Color(0xFF616161)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color(0xFF6200EE),
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color(0xFFC5CAE9)
            )
//
        )
    }
}

@Composable
fun GooglePayDetails() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            "Google Pay Selected",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF616161)),
            modifier = Modifier.padding(8.dp)
        )
        // Add more details if needed
    }
}

@Composable
fun BankTransferDetails() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        TextField(
            value = "",
            onValueChange = { /* Handle bank account number change */ },
            label = { Text("Bank Account Number", color = Color(0xFF616161)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color(0xFF6200EE),
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color(0xFFC5CAE9)
            )
//
        )
        TextField(
            value = "",
            onValueChange = { /* Handle bank routing number change */ },
            label = { Text("Routing Number", color = Color(0xFF616161)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color(0xFF6200EE),
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color(0xFFC5CAE9)
            )
//
        )
    }
}
