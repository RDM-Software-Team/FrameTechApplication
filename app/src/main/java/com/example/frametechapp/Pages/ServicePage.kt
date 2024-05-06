package com.example.frame_tech_app.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun ServicePage(){
 Column(
      modifier = Modifier.verticalScroll(rememberScrollState(),true)
 ) {
  Text(text = "Service")
  ServiceFrame()
 }
}

@Composable
fun ServiceFrame(){
 Column {
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
    .fillMaxWidth().border(1.dp , Color.Black)
  ) {
   OutlinedTextField(
    value = "null",
    onValueChange = { it },
    label = { Text(text = "Enter Name of Problem")}

   )//Name of a problem.
   OutlinedTextField(
    value = "null",
    onValueChange = { it },
    label = { Text(text = "Description of the problem")},
    minLines = 3,
    maxLines = 40
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
     onClick = { /*TODO*/ },
     modifier = Modifier
      .size(50.dp)
      .border(2.dp, Color.Gray, RoundedCornerShape(50)),
     colors = IconButtonDefaults.iconButtonColors(Color.LightGray),
    ) {
     Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Image", tint = Color.White)
    }
   }

   Button(
    onClick = { /*TODO*/ },
    shape = RectangleShape,
    colors = ButtonColors(
     containerColor = Color.Blue,
     contentColor = Color.White,
     disabledContentColor = Color.Black, disabledContainerColor = Color.LightGray
    )
   ) {
    Text(text = "Send")
   }
  }


  //Internet Services..
 }
}