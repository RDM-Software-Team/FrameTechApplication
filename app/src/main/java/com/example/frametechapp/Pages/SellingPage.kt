package com.example.frametechapp.Pages

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.frametechapp.Data.SellsReports
import com.example.frametechapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SellingPage() {
    val context = LocalContext.current

    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var buttonMakeSell by remember { mutableStateOf(false) }
    var imageBackUri by remember { mutableStateOf<Uri?>(null) }
    var imageFrontUri by remember { mutableStateOf<Uri?>(null) }
    var imageArielUri by remember { mutableStateOf<Uri?>(null) }

    val launcherBack = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageBackUri = uri
    }
    val launcherFront = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageFrontUri = uri
    }
    val launcherAriel = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageArielUri = uri
    }

    val sellReports by remember { mutableStateOf(mutableListOf<SellsReports>()) }

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

                LazyRow {
                    item { ImagePicker(imageUri = imageBackUri, label = "Back Photo", launcher = { launcherBack.launch("image/*") }) }
                    item { ImagePicker(imageUri = imageFrontUri, label = "Front Photo", launcher = { launcherFront.launch("image/*") }) }
                    item { ImagePicker(imageUri = imageArielUri, label = "Side Photo", launcher = { launcherAriel.launch("image/*") }) }
                }

                Button(
                    onClick = {
                        if (imageBackUri != null && imageFrontUri != null && imageArielUri != null) {
                            sellReports.add(
                                SellsReports(
                                    productName = productName,
                                    productDescription = productDescription,
                                    productImages = listOf(imageBackUri, imageFrontUri, imageArielUri),
                                    price = 0.0,
                                    condition = Condition.NEW,
                                    type = ProductType.ELECTRONICS
                                )
                            )
                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                            buttonMakeSell = false
                            productName = ""
                            productDescription = ""
                        } else {
                            Toast.makeText(context, "Please upload all images", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Text(text = "Send", color = Color.White)
                }
            }
        }

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

        SellProcess(sellReports)
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
            sellReports.forEachIndexed { index, report ->
                var statusIndex by remember { mutableStateOf(0) }
                var price by remember { mutableStateOf(0.0) }

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
                            progress = (statusIndex + 1) / statuses.size.toFloat(),
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
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
