package com.example.frametechapp.Pages

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.frametechapp.Data.RepairRequest
import com.example.frametechapp.R

@Preview(showBackground = true)
@Composable
fun ServicePage(){
 Column(
      modifier = Modifier.verticalScroll(rememberScrollState(),true)
 ) {
  ServiceFrame()
 }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ServiceFrame(){
 val context = LocalContext.current
 val problemName = remember {
   mutableStateOf("")
 }
 val problemDescription = remember {
  mutableStateOf("")
 }
 val serviceAddImage = remember {
   mutableStateOf(false)
 }
 val chosenPrice = remember {//Will be saving the price chosen by the user for internet tine
    mutableStateOf<Prices>(Prices.Prices0)
 }
 val isBookInternetClick = remember {
      mutableStateOf(false)
 }
 val storeRepair by remember {//This will be taking the input by the user for the repair Service
  mutableStateOf(mutableListOf<RepairRequest>())
 }
 var imageUri by remember { mutableStateOf<Uri?>(null) }//This will be storing the uri for the image chosen from the device gallery
 val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
  imageUri = uri //this set the uri to the imageUri
 }
 Column(
   modifier = Modifier.fillMaxSize(),
  verticalArrangement = Arrangement.Center,
  horizontalAlignment = Alignment.CenterHorizontally
 ) {
  Text(
   text = "Welcome to Computer Complex Service.",
   textAlign = TextAlign.Center,
   textDecoration = TextDecoration.Underline,
   fontWeight = FontWeight(450),
   fontFamily = FontFamily.Cursive,
   modifier = Modifier.fillMaxWidth()
  )

  //Repair Services..
  Column(
   modifier = Modifier
    .padding(10.dp)
    .border(1.dp, Color.Black)
    .fillMaxWidth()
    .border(1.dp, Color.Black)
  ) {
   OutlinedTextField(
    value = problemName.value,
    isError = problemName.value.isBlank(),
    onValueChange = { problemName.value = it },
    label = { Text(text = "Enter Name of Problems")},
    modifier = Modifier.padding(10.dp)

   )//Name of a problem.
   OutlinedTextField(
    isError = problemDescription.value.isBlank(),
    value = problemDescription.value,
    onValueChange = { problemDescription.value = it  },
    label = { Text(text = "Description of the problem")},
    minLines = 3,
    maxLines = 40,
    modifier = Modifier.padding(10.dp)
   )//Description of problem.
   Column(
    modifier = Modifier
     .padding(10.dp)
     .width(180.dp)
     .height(200.dp)
     .border(1.dp, Color.Black)
     .background(Color.Transparent),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,

    ) {
    IconButton(
     onClick = {
      launcher.launch("image/*")//this will launch the gallery to choose an image
     },
     modifier = Modifier
      .size(50.dp)
      .border(2.dp, Color.Gray, RoundedCornerShape(50)),
     colors = IconButtonDefaults.iconButtonColors(Color.LightGray),
     enabled = !serviceAddImage.value
    ) {
     Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Image", tint = Color.White)
    }
    Image(
     painter = if(imageUri != null) {
      rememberAsyncImagePainter(imageUri)
     }else {
      painterResource(id = R.drawable.default_image)
      },
     contentDescription = null,
     modifier = Modifier.fillMaxSize()
    )
   }

   Button(
    onClick = {
              if(problemName.value.isNotEmpty() || problemDescription.value.isNotEmpty()){
               storeRepair.add(RepairRequest(
                "",
                problemName = problemName.value,
                problemDescription = problemDescription.value, )
               )
               Toast.makeText(context,"Saved",Toast.LENGTH_LONG).show()
               //When saved it must return to empty state
               problemName.value = ""
               problemDescription.value = ""
              }
              Toast.makeText(context,"Please fill in the blank Text Field",Toast.LENGTH_LONG).show()
              problemName.value = ""
              problemDescription.value = ""

    },
    shape = RoundedCornerShape(15.dp),
    colors = ButtonColors(
     containerColor = Color.Blue,
     contentColor = Color.White,
     disabledContentColor = Color.Black, disabledContainerColor = Color.LightGray
    ),

    modifier = Modifier
     .padding(start = 10.dp)
     .width(100.dp)
   ) {
    Text(text = "Send")
   }
  }


  //Internet Services..
  Column(
     modifier = Modifier
      .fillMaxHeight()
      .border(1.dp, Color.Black)
      .padding(10.dp),
     verticalArrangement = Arrangement.Center,
     horizontalAlignment = Alignment.CenterHorizontally
  ) {
   Text(
    text = "Internet Service...",
    textDecoration = TextDecoration.Underline,
    textAlign = TextAlign.Center,
    fontWeight = FontWeight(800),
    fontFamily = FontFamily.SansSerif,
    modifier = Modifier.fillMaxWidth()
   )
   //selectableOptions()
   for (i in selectableOptions()) {
    chosenPrice.value = i
   }

   if (isBookInternetClick.value) {
    if (chosenPrice.value != Prices.Prices0) {
     Text(
      text = "${checkInternetTime(price = chosenPrice.value).value} minutes",
      textAlign = TextAlign.Center,
      modifier = Modifier.fillMaxWidth()
     )
    } else {
     Text(
      text = "",
      textAlign = TextAlign.Center,
      modifier = Modifier.fillMaxWidth()
     )
    }
   }
   Button(onClick = {
    //checkInternetTime(price = chosenPrice.value)
    isBookInternetClick.value = true
    Toast.makeText(context, "wow", Toast.LENGTH_LONG).show()
   }) {
    Text(text = "Book Internet Time ")
   }
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
fun checkInternetTime(price: Prices):MutableState<Int>{
 //This function will be used to display the time depending on the price
      val timeByCash = remember {
          mutableIntStateOf(0)
      }
      when(price) {
       Prices.Prices1 -> timeByCash.intValue = 30
       Prices.Prices2 -> timeByCash.intValue = 60
       Prices.Prices3 -> timeByCash.intValue = 90
       Prices.Prices4 -> timeByCash.intValue = 120
       Prices.Prices5 -> timeByCash.intValue = 150
       Prices.Prices0 -> timeByCash.intValue =0
      }
      return  timeByCash
}