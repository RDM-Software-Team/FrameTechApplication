package com.example.frametechapp.Pages

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.frametechapp.Controller.SessionViewModel
import com.example.frametechapp.Data.SellListing
import com.example.frametechapp.Data.SellsReports
import com.example.frametechapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SellingPage(sessionViewModel: SessionViewModel) {
    val context = LocalContext.current

    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var productType by remember { mutableStateOf(ProductType.ELECTRONICS) }
    var condition by remember { mutableStateOf(Condition.NEW) }
    var buttonMakeSell by remember { mutableStateOf(false) }
    var imageBackUri by remember { mutableStateOf<Uri?>(null) }
    var imageFrontUri by remember { mutableStateOf<Uri?>(null) }
    var imageArielUri by remember { mutableStateOf<Uri?>(null) }
    val isSubmitting = remember { mutableStateOf(false) }

    //Launching image picker
    val launcherBack = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageBackUri = uri
    }
    val launcherFront = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageFrontUri = uri
    }
    val launcherAriel = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageArielUri = uri
    }
    // State to hold the fetched sell listings
    val sellListings = remember { mutableStateListOf<SellListing>() }

    // Fetch sell listings when the composable is first displayed
    LaunchedEffect(Unit) {
        sessionViewModel.viewSellListings(
            onSuccess = { listings ->
                sellListings.clear()
                sellListings.addAll(listings)
            },
            onError = { error ->
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome to Sell to Us",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Animated Visibility for Sell Form
        AnimatedVisibility(visible = buttonMakeSell) {
            Column {
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text(text = "Enter Product Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = productDescription,
                    onValueChange = { productDescription = it },
                    label = { Text(text = "Enter Product Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                // Price Input Field
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text(text = "Enter Product Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                // Product Type Dropdown
                DropdownSelector(
                    label = "Select Product Type",
                    options = ProductType.entries,
                    selectedOption = productType,
                    onOptionSelected = { productType = it }
                )

                // Condition Dropdown
                DropdownSelector(
                    label = "Select Condition",
                    options = Condition.entries,
                    selectedOption = condition,
                    onOptionSelected = { condition = it }
                )

                LazyRow {
                    item { ImagePicker(imageUri = imageBackUri, label = "Back Photo", launcher = { launcherBack.launch("image/*") }) }
                    item { ImagePicker(imageUri = imageFrontUri, label = "Front Photo", launcher = { launcherFront.launch("image/*") }) }
                    item { ImagePicker(imageUri = imageArielUri, label = "Side Photo", launcher = { launcherAriel.launch("image/*") }) }
                }

                // Submit Button
                Button(
                    onClick = {
                        if (imageBackUri != null && imageFrontUri != null && imageArielUri != null && price.isNotEmpty()) {
                            isSubmitting.value = true

                            // Prepare image paths for the sell request
                            val imagePaths = listOf(
                                imageBackUri!!,
                                imageFrontUri!!,
                                imageArielUri!!
                            )

                            // Call ViewModel's sellItems function
                            sessionViewModel.sellItems(
                                description = productDescription,
                                price = price, // Dynamic price
                                imagePaths = imagePaths,
                                onSuccess = {
                                    Toast.makeText(context, "Item listed for sale", Toast.LENGTH_SHORT).show()
                                    buttonMakeSell = false
                                    productName = ""
                                    productDescription = ""
                                    price = ""
                                    imageBackUri = null
                                    imageFrontUri = null
                                    imageArielUri = null
                                    isSubmitting.value = false
                                },
                                onError = { errorMessage ->
                                    Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                                    isSubmitting.value = false
                                }
                            )
                        } else {
                            Toast.makeText(context, "Please upload all images and enter a price", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                    enabled = !isSubmitting.value
                ) {
                    if (isSubmitting.value) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text(text = "Send", color = Color.White)
                    }
                }
            }
        }

        // Button to Trigger Sell Form
        AnimatedVisibility(visible = !buttonMakeSell) {
            Button(
                onClick = { buttonMakeSell = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(text = "Make a Sell", color = Color.White)
            }
        }

        // Display the fetched sell listings
        SellListings(sellListings = sellListings)
    }
}

@Composable
fun SellListings(sellListings: List<SellListing>) {
    //val context = LocalContext.current
    var selectedListing by remember { mutableStateOf<SellListing?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        items(sellListings) { listing ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { selectedListing = listing },
                elevation = CardDefaults.elevatedCardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(listing.image1Base64),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = listing.description, fontWeight = FontWeight.Bold)
                        Text(text = "Price: $${listing.price}")
                    }
                }
            }
        }
    }

    // Show popup with full details if an item is clicked
    if (selectedListing != null) {
        SellDetailsPopup(listing = selectedListing!!, onDismiss = { selectedListing = null })
    }
}

@Composable
fun SellDetailsPopup(listing: SellListing, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Product Details", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))

                Image(
                    painter = rememberAsyncImagePainter(listing.image1Base64),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Description: ${listing.description}", fontWeight = FontWeight.Bold)
                Text(text = "Price: $${listing.price}")
                //Text(text = "Condition: ${listing}")
                //Text(text = "Type: ${listing}")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { onDismiss() }) {
                    Text(text = "Close")
                }
            }
        }
    }
}

@Composable
fun ImagePicker(imageUri: Uri?, label: String, launcher: () -> Unit) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .padding(8.dp)
            .border(2.dp, Color.Gray, RoundedCornerShape(12.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageUri == null) {
            IconButton(onClick = { launcher() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = label)
                Text(text = label, style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { launcher() }
            )
        }
    }
}

// DropdownSelector Composable for Product Type and Condition
@Composable
fun <T> DropdownSelector(
    label: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) where T : Enum<T> {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "$label: ${selectedOption.name}", modifier = Modifier.fillMaxWidth())
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(text = option.name)
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun SellProcess(sellReports: List<SellsReports>) {
    val statuses = listOf("Processing", "Reviewing", "Approved", "Denied")
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Preload the sound effect
    mediaPlayer = MediaPlayer.create(context, R.raw.approval_sound)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (sellReports.isEmpty()) {
            Text(
                text = "No products are being sold to us :)",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        } else {
            sellReports.forEachIndexed { _, report ->
                var statusIndex by remember { mutableIntStateOf(0) }
                var price by remember { mutableDoubleStateOf(0.0) }

                // Simulate status updates with a coroutine
                LaunchedEffect(key1 = Unit) {
                    scope.launch {
                        while (statusIndex < statuses.size - 1) {
                            delay(3000L) // Wait 3 seconds before each status update
                            statusIndex++

                            // Trigger notifications on "Approved" or "Denied"
                            if (statuses[statusIndex] == "Approved" || statuses[statusIndex] == "Denied") {
                                // Toast notification
                                Toast.makeText(
                                    context,
                                    "Product ${report.productName} has been ${statuses[statusIndex]}!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Play sound for "Approved" status
                                if (statuses[statusIndex] == "Approved") {
                                    mediaPlayer?.start()
                                }
                            }
                        }
                    }
                }

                // Calculate the price based on product condition and type
                price = calculatePrice(report)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(report.productImages.firstOrNull() ?: painterResource(R.drawable.default_image)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(8.dp)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Text(text = report.productName, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Price: $${String.format("%.2f", price)}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Condition: ${report.condition}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Type: ${report.type}", style = MaterialTheme.typography.bodyMedium)
                    }
                    Text(
                        text = statuses[statusIndex],
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    if (statusIndex < statuses.size - 1) {
                        CircularProgressIndicator(
                            progress = { (statusIndex + 1) / statuses.size.toFloat() },
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp),
                            trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                        )
                    }
                }
            }
        }
    }
}

// Custom price calculation based on condition and type
fun calculatePrice(report: SellsReports): Double {
    val basePrice = when (report.condition) {
        Condition.NEW -> 100.0
        Condition.USED -> 70.0
        Condition.DAMAGED -> 40.0
    }
    val typeMultiplier = when (report.type) {
        ProductType.ELECTRONICS -> 1.5
        ProductType.CLOTHING -> 1.0
        ProductType.FURNITURE -> 1.2
    }
    // Additional price bonus for the number of images
    val imageBonus = report.productImages.size * 10.0

    return basePrice * typeMultiplier + imageBonus
}

enum class Condition {
    NEW, USED, DAMAGED
}

enum class ProductType {
    ELECTRONICS, CLOTHING, FURNITURE
}
