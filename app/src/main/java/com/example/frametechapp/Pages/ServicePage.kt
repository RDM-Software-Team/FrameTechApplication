package com.example.frametechapp.Pages

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.frametechapp.Controller.SessionViewModel
import com.example.frametechapp.Data.RepairRequest
import com.example.frametechapp.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun ServicePage(sessionViewModel: SessionViewModel) {
 Column(
  modifier = Modifier
   .verticalScroll(rememberScrollState())
   .fillMaxSize()
   .padding(16.dp)
 ) {
  ServiceFrame(sessionViewModel)
 }
}

@Composable
fun ServiceFrame(sessionViewModel: SessionViewModel) {
 val context = LocalContext.current
 val problemName = remember { mutableStateOf("") }
 val problemDescription = remember { mutableStateOf("") }
 val isBookInternetClick = remember { mutableStateOf(false) }
 val chosenPrice = remember { mutableStateOf(Prices.Prices0) }
 var imageUri by remember { mutableStateOf<Uri?>(null) }
 val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
  imageUri = uri
 }

 val isSending = remember { mutableStateOf(false) }
 val repairRequests = remember { mutableStateListOf<RepairRequest>() } // For fetched repair requests

 Column(
  modifier = Modifier
   .fillMaxSize()
   .padding(16.dp),
  verticalArrangement = Arrangement.Center,
  horizontalAlignment = Alignment.CenterHorizontally
 ) {
  Text(
   text = "Welcome to Computer Complex Service.",
   textAlign = TextAlign.Center,
   fontWeight = FontWeight.Bold,
   fontSize = 24.sp,
   modifier = Modifier
    .fillMaxWidth()
    .padding(bottom = 16.dp)
  )

  // Image Preview
  AnimatedVisibility(visible = imageUri != null) {
   Image(
    painter = imageUri?.let { rememberAsyncImagePainter(it) } ?: painterResource(id = R.drawable.default_image),
    contentDescription = null,
    modifier = Modifier
     .fillMaxWidth()
     .height(200.dp)
     .padding(8.dp)
     .clip(RoundedCornerShape(10.dp))
     .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
     .background(Color.LightGray)
   )
  }

  // Add Image Button
  val buttonElevation by animateDpAsState(if (imageUri != null) 12.dp else 4.dp, label = "buttonElevation")
  Button(
   onClick = {
    launcher.launch("image/*")
   },
   modifier = Modifier
    .padding(16.dp)
    .size(60.dp),
   shape = CircleShape,
   elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = buttonElevation)
  ) {
   Icon(Icons.Default.Add, contentDescription = "Add Image")
  }

  // Problem Name Input
  OutlinedTextField(
   value = problemName.value,
   onValueChange = { problemName.value = it },
   label = { Text(text = "Enter Problem Name") },
   modifier = Modifier
    .fillMaxWidth()
    .padding(8.dp),
   isError = problemName.value.isBlank()
  )

  // Problem Description Input
  OutlinedTextField(
   value = problemDescription.value,
   onValueChange = { problemDescription.value = it },
   label = { Text(text = "Enter Problem Description") },
   modifier = Modifier
    .fillMaxWidth()
    .padding(8.dp),
   isError = problemDescription.value.isBlank(),
   maxLines = 5
  )

  // Send Button with animation
  val buttonBackgroundColor by animateColorAsState(
   if (problemName.value.isNotEmpty() && problemDescription.value.isNotEmpty()) Color.Blue else Color.Gray,
   label = "buttonBackgroundColor"
  )

  Button(
   onClick = {
    if (problemName.value.isNotEmpty() && problemDescription.value.isNotEmpty()) {
     if (imageUri != null) {
      isSending.value = true
      val imagePath = imageUri?.path ?: ""

      // Call the ViewModel to send the repair request
      sessionViewModel.requestRepair(
       description = problemDescription.value,
       bookedDate = "2024-09-01", // Example booked date, change this dynamically
       imageUri = imageUri!!, // Pass the Uri directly, not as a String
       onSuccess = {
        Toast.makeText(context, "Repair Request Sent", Toast.LENGTH_LONG).show()
        problemName.value = ""
        problemDescription.value = ""
        imageUri = null
        isSending.value = false
       },
       onError = { errorMessage ->
        Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
        isSending.value = false
       }
      )
     } else {
      Toast.makeText(context, "Please add an image", Toast.LENGTH_SHORT).show()
     }
    } else {
     Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
    }
   },
   modifier = Modifier
    .padding(8.dp)
    .fillMaxWidth(),
   shape = RoundedCornerShape(12.dp),
   colors = ButtonDefaults.buttonColors(
    containerColor = buttonBackgroundColor,
    contentColor = Color.White,
    disabledContainerColor = Color.Black,
   ),
   enabled = !isSending.value
  ) {
   if (isSending.value) {
    CircularProgressIndicator(color = Color.White)
   } else {
    Text(text = "Send", fontWeight = FontWeight.Bold)
   }
  }

  // Button to fetch repair requests
  Button(
   onClick = {
    sessionViewModel.viewRepairRequests(
     onSuccess = { requests ->
      repairRequests.clear()
      repairRequests.addAll(requests)
     },
     onError = { error ->
      Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
     }
    )
   },
   modifier = Modifier
    .fillMaxWidth()
    .padding(8.dp),
   colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
  ) {
   Text(text = "View Repair Requests", color = Color.White)
  }

  // Display the list of repair requests
  if (repairRequests.isNotEmpty()) {
   RepairRequestList(repairRequests = repairRequests)
  } else {
   Text(
    text = "No repair requests found.",
    style = MaterialTheme.typography.bodyLarge,
    modifier = Modifier.padding(16.dp)
   )
  }

  HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

  // Internet Services Section
  Text(
   text = "Internet Service",
   fontWeight = FontWeight.Bold,
   fontSize = 18.sp,
   modifier = Modifier
    .fillMaxWidth()
    .padding(8.dp)
  )

  selectableOptions()

  if (isBookInternetClick.value && chosenPrice.value != Prices.Prices0) {
   Text(
    text = "${checkInternetTime(price = chosenPrice.value).value} minutes",
    textAlign = TextAlign.Center,
    modifier = Modifier
     .fillMaxWidth()
     .padding(8.dp)
   )
  }

  Button(
   onClick = {
    isBookInternetClick.value = true
   },
   modifier = Modifier
    .padding(8.dp)
    .fillMaxWidth()
  ) {
   Text(text = "Book Internet Time", fontWeight = FontWeight.Bold)
  }
 }
}

@Composable
fun RepairRequestList(repairRequests: List<RepairRequest>) {
 val context = LocalContext.current

 LazyColumn(
  modifier = Modifier
   .fillMaxWidth()
   .height(2000.dp)
 ) {
  items(repairRequests) { repairRequest ->
   Card(
    modifier = Modifier
     .fillMaxWidth()
     .padding(8.dp),
    elevation = CardDefaults.elevatedCardElevation(4.dp)
   ) {
    Column(
     modifier = Modifier
      .padding(16.dp)
      .fillMaxWidth()
    ) {
     AsyncImage(model = repairRequest.profileImage, contentDescription = "image")
     Text(text = "Problem: ${repairRequest.problemName}", fontWeight = FontWeight.Bold)
     Text(text = "Description: ${repairRequest.problemDescription}")
     Text(text = "Date: ${repairRequest.bookedDate}")
     Spacer(modifier = Modifier.height(8.dp))

     // Button to generate ticket (PDF or image)
     Button(
      onClick = {
       generateTicket(context, repairRequest)
      },
      modifier = Modifier.fillMaxWidth(),
      colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
     ) {
      Text(text = "Generate Ticket", color = Color.White)
     }
    }
   }
  }
 }
}

// Function to generate ticket as PDF or image
fun generateTicket(context: Context, repairRequest: RepairRequest) {
 val pdfDocument = PdfDocument()
 val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
 val page = pdfDocument.startPage(pageInfo)

 val canvas = page.canvas
 val paint = Paint()

 paint.textSize = 16f
 paint.color = Color.Black.hashCode()

 canvas.drawText("Repair Request Ticket", 10f, 25f, paint)
 canvas.drawText("Problem: ${repairRequest.problemName}", 10f, 60f, paint)
 canvas.drawText("Description: ${repairRequest.problemDescription}", 10f, 90f, paint)
 canvas.drawText("Date: ${repairRequest.bookedDate}", 10f, 120f, paint)

 // Finish the page
 pdfDocument.finishPage(page)

 // Create the output file
 val directory = File(context.getExternalFilesDir(null), "Tickets")
 if (!directory.exists()) {
  directory.mkdirs()
 }

 val file = File(directory, "Repair_Ticket_${repairRequest.repairId}.pdf")

 try {
  pdfDocument.writeTo(FileOutputStream(file))
  Toast.makeText(context, "Ticket generated: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
 } catch (e: IOException) {
  Toast.makeText(context, "Error generating ticket: ${e.message}", Toast.LENGTH_SHORT).show()
 }

 // Close the document
 pdfDocument.close()
}


//Payment prices options
enum class  Prices(val price:Double){//This object contains the prices for the internet times
Prices0(0.0),
 Prices1(5.00),
 Prices2(10.00),
 Prices3(15.00),
 Prices4(20.00),
 Prices5(25.00)
}
@Composable
fun selectableOptions(): SnapshotStateList<Prices> {
 val selectedOptions = remember { mutableStateListOf<Prices>() }

 Row(
  modifier = Modifier.padding(10.dp)
 ) {
  CheckboxOption(price = Prices.Prices1, selectedOptions)
  CheckboxOption(price = Prices.Prices2, selectedOptions)
  CheckboxOption(price = Prices.Prices3, selectedOptions)
  CheckboxOption(price = Prices.Prices4, selectedOptions)
 }

 return selectedOptions
}

@Composable
fun CheckboxOption(
 price: Prices,
 selectedOptions: MutableList<Prices>
) {
 val checked = remember { mutableStateOf(false) }

 Row(
  verticalAlignment = Alignment.CenterVertically,
  modifier = Modifier
   .fillMaxHeight()
   .padding(start = 0.dp)
 ) {
  Checkbox(
   checked = checked.value,
   onCheckedChange = { isChecked ->
    checked.value = isChecked
    if (isChecked) {
     selectedOptions.add(price)
    } else {
     selectedOptions.remove(price)
    }
   }
  )
  Text(
   text = "R.${price.price}",
   style = MaterialTheme.typography.labelSmall,
  )
 }
}
@Composable
fun checkInternetTime(price: Prices):MutableState<Int> {
 //This function will be used to display the time depending on the price
 val timeByCash = remember {
  mutableIntStateOf(0)
 }
 when (price) {
  Prices.Prices1 -> timeByCash.intValue = 30
  Prices.Prices2 -> timeByCash.intValue = 60
  Prices.Prices3 -> timeByCash.intValue = 90
  Prices.Prices4 -> timeByCash.intValue = 120
  Prices.Prices5 -> timeByCash.intValue = 150
  Prices.Prices0 -> timeByCash.intValue = 0
 }
 return timeByCash
}