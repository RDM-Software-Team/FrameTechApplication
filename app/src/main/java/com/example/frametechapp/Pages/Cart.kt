package com.example.frametechapp.Pages

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stripe.android.paymentsheet.PaymentSheetResult
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.frame_tech_app.Data.CartItem
import com.example.frametechapp.Controller.PaymentMethod
import com.example.frametechapp.Controller.PaymentUiState
import com.example.frametechapp.Controller.PaymentViewModel
import com.example.frametechapp.Controller.SessionViewModel
import com.stripe.android.paymentsheet.PaymentSheet

@Composable
fun Cart(sessionViewModel: SessionViewModel, navController: NavController) {
    //val cartItems by sessionViewModel.cartItems
    val context = LocalContext.current
    // Collect cartItems and cartMessage as StateFlows
    val cartItems by sessionViewModel.cartItems.collectAsState(emptyList())  // Default to empty list
    val cartMessage by sessionViewModel.cartMessage.collectAsState(null)    // Null if no message
    // Show the cart message as a Toast whenever it is not null
    cartMessage?.let {
        LaunchedEffect(it) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            sessionViewModel.clearCartMessage()  // Optionally clear the message after showing it
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
                    onClick = { navController.navigate("checkout") },
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


@Composable
fun PaymentScreen(viewModel: PaymentViewModel = PaymentViewModel()) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val paymentMethods by viewModel.paymentMethods.collectAsState()

    var amount by remember { mutableStateOf("10.00") }
    var currency by remember { mutableStateOf("USD") }

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
            OutlinedTextField(
                value = currency,
                onValueChange = { currency = it },
                label = { Text("Currency") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        items(paymentMethods) { method ->
            PaymentMethodItem(method)
        }

        item {
            Button(
                onClick = {
                    viewModel.initiatePayment(amount.toDoubleOrNull() ?: 0.0, currency)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Pay Now")
            }
        }

        item {
//            when (val state = uiState) {
//                is PaymentUiState.Loading -> CircularProgressIndicator()
//                is PaymentUiState.Error -> Text("Error: ${state.message}")
//                is PaymentUiState.PaymentReady -> {
//                    LaunchedEffect(state) {
//                        val paymentSheet = PaymentSheet(context, ::onPaymentResult)
//                        val paymentIntentClientSecret = state.clientSecret
//                        paymentSheet.presentWithPaymentIntent(
//                            paymentIntentClientSecret,
//                            PaymentSheet.Configuration(
//                                merchantDisplayName = "Your App Name",
//                                customer = null,
//                                allowsDelayedPaymentMethods = true
//                            )
//                        )
//                    }
//                }
//                is PaymentUiState.PaymentSuccess -> Text("Payment Successful!")
//                else -> {}
//            }

      }
    }
}
@Composable
fun PaymentMethodItem(method: PaymentMethod) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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
}
