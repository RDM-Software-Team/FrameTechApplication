package com.example.frametechapp.Pages

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.frametechapp.Data.RepairRequest
import com.example.frametechapp.R

@Preview(showBackground = true)
@Composable
fun ServicePage() {
 Column(
  modifier = Modifier
   .verticalScroll(rememberScrollState())
   .fillMaxSize()
   .padding(16.dp)
 ) {
  ServiceFrame()
 }
}

@Composable
fun ServiceFrame() {
 val context = LocalContext.current
 val problemName = remember { mutableStateOf("") }
 val problemDescription = remember { mutableStateOf("") }
 val serviceAddImage = remember { mutableStateOf(false) }
 val chosenPrice = remember { mutableStateOf(Prices.Prices0) }
 val isBookInternetClick = remember { mutableStateOf(false) }
 val storeRepair by remember { mutableStateOf(mutableListOf<RepairRequest>()) }
 var imageUri by remember { mutableStateOf<Uri?>(null) }
 val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
  imageUri = uri
 }

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

  // Button with an animation on click
  val buttonElevation by animateDpAsState(if (serviceAddImage.value) 12.dp else 4.dp)
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
   isError = problemName.value.isBlank(),
   onValueChange = { problemName.value = it },
   label = { Text(text = "Enter Problem Name") },
   modifier = Modifier
    .fillMaxWidth()
    .padding(8.dp)
  )

  // Problem Description Input
  OutlinedTextField(
   value = problemDescription.value,
   isError = problemDescription.value.isBlank(),
   onValueChange = { problemDescription.value = it },
   label = { Text(text = "Description of the Problem") },
   modifier = Modifier
    .fillMaxWidth()
    .padding(8.dp),
   maxLines = 5
  )

  // Send Button with animation
  val buttonBackgroundColor by animateColorAsState(
   if (problemName.value.isNotEmpty() && problemDescription.value.isNotEmpty()) Color.Blue else Color.Gray
  )
  Button(
   onClick = {
    if (problemName.value.isNotEmpty() && problemDescription.value.isNotEmpty()) {
     storeRepair.add(
      RepairRequest(
       repairId = 10,
       problemName = problemName.value,
       problemDescription = problemDescription.value
      )
     )
     Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show()
     problemName.value = ""
     problemDescription.value = ""
    } else {
     Toast.makeText(context, "Please fill in the blank Text Fields", Toast.LENGTH_LONG).show()
    }
   },
   modifier = Modifier
    .padding(8.dp)
    .fillMaxWidth(),
   shape = RoundedCornerShape(12.dp),
   colors = ButtonDefaults.buttonColors(
    disabledContentColor = buttonBackgroundColor,
    containerColor = buttonBackgroundColor,
    contentColor = Color.White,
    disabledContainerColor = Color.Black,
   )
  ) {
   Text(text = "Send", fontWeight = FontWeight.Bold)
  }

  Divider(modifier = Modifier.padding(vertical = 16.dp))

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
    modifier = Modifier.fillMaxWidth().padding(8.dp)
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