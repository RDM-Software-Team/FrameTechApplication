package com.example.frame_tech_app.Pages

import android.content.Context
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frametech_app.Data.items
import com.example.frametechapp.Model.CateoryFilter
@Preview(showBackground = true)
@Composable
fun Cart(){
 Column(
  modifier = Modifier.fillMaxSize(),
  horizontalAlignment = Alignment.CenterHorizontally,
  verticalArrangement = Arrangement.Center
 ) {
  Text(text = "Cart")
  val listGetters = CateoryFilter()
  var CartList by remember {mutableStateOf(listGetters.newItemList)}
  CartTable(cartItems = CartList) { itemId ->//this is the delete function
   CartList = CartList.filter { it.itemId != itemId }
  }
 }
}
@Composable
fun CartTable(cartItems: List<items>, onDelete: (Int) -> Unit){
 val context = LocalContext.current
 val columnNames = listOf("Item Name", "Quantity", "Price","SubTotal" ,"Delete")
 var isDelete = remember { mutableStateOf(false) }
 var quantity = remember { mutableStateOf(1) }
  Column(
    modifier = Modifier.fillMaxSize()
  ) {
   //This column or the header section of the table
   LazyRow(
    modifier = Modifier.fillMaxWidth()
   ) {
      items(columnNames){ it ->
         Text(text = it, modifier = Modifier
          .padding(10.dp)
          .weight(1f, true))
      }
   }
   //The is the Row Section of the table
    LazyColumn{
     items(cartItems) { item ->
      Row(
       modifier = Modifier
        .fillMaxWidth()
        .padding(7.dp)
        .align(Alignment.CenterHorizontally)
       ,
       horizontalArrangement = Arrangement.SpaceBetween,
       verticalAlignment = Alignment.CenterVertically
      ) {
       Text(text = item.itemName,modifier = Modifier
        .border(1.dp, Color.Black)
        .width(80.dp)
        .fillMaxHeight())
       OutlinedTextField(
        value = "${quantity.value}",
        onValueChange = { quantity.value = it.toInt() },
        modifier = Modifier
         .padding(16.dp)
         .width(50.dp),
        textStyle = TextStyle(fontSize = 12.sp)
       )
       Text(
        text = "R."+item.itemPrice.toString()
        , modifier = Modifier
         .border(1.dp, Color.Black)
         .width(80.dp)
         .fillMaxHeight()
       )
       Text(
        text = "R." + (item.itemPrice * quantity.value).toString(),
         modifier = Modifier
          .border(1.dp, Color.Black)
          .width(80.dp)
          .fillMaxHeight()
       )
       IconButton(onClick = {
        onDelete(item.itemId)
        Toast.makeText(context, "${item.itemId} + ${item.itemName}", Toast.LENGTH_SHORT).show()
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
}

fun checkout(){

}