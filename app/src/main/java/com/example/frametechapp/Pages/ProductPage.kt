package com.example.frametechapp.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frametech_app.Data.items
import com.example.frametechapp.Model.CategoryFilter
import com.example.frametechapp.R

@Preview(showBackground = true)
@Composable
fun ProductPage(){
 Column {
  //Text(text = "ProductPage")
  MainFrame()
 }
}

@Composable
fun MainFrame(){
    val searchValue = remember { mutableStateOf("") }
    val get = CategoryFilter()
 Column(
  modifier = Modifier
      .verticalScroll(rememberScrollState(), enabled = true)
      .background(Color.LightGray)
      .size(1200.dp, 4500.dp)
      .padding(horizontal = 8.dp)
 ) {
  //top options to search and filter

  Row(
   modifier = Modifier.width(900.dp),
   verticalAlignment = Alignment.CenterVertically,
   horizontalArrangement = Arrangement.SpaceBetween
  ){
   TextField(
    value = searchValue.value,
    onValueChange = { searchValue.value = it },
    leadingIcon = {
     IconButton(onClick = {}) {
      Icon(Icons.Filled.Search, contentDescription = "Icon")//Need to make a working search function/action..
     }
    },
    maxLines = 2,
    modifier = Modifier
        .width(220.dp)
        .height(50.dp)
   )

   Button(onClick = { /*TODO*/ },
    ) {
    Icon(imageVector = Icons.AutoMirrored.Filled.Sort, contentDescription = null)
    Text(text = "Filter")
   }
  }
     HorizontalDivider(
         Modifier
             .background(Color.Black)
             .padding(2.dp)
     )
  //Bottom view to display the items..
   ProductView()
 }
}
@Composable
fun ProductView() {
    val context = LocalContext.current
    val get = CategoryFilter()

    val groupLists = remember {
        mutableStateOf(listOf<List<items>>(get.favList,get.newItemList,get.unBroghtItems))
    }//This collects all List<items> in the system.


    Column {
        groupLists.value.forEach { itemList ->
            Text(
                text = itemList.firstOrNull()?.itemCategory ?: "New ",
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(itemList) { product ->
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Image(
                            painter = painterResource(R.drawable.dell_latitude_5430),
                            contentDescription = product.itemName,
                            modifier = Modifier.width(290.dp)
                        )
                        Text(
                            text = product.itemName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight(700),
                        )
                        Text(text = "R. ${product.itemPrice}")
                        OutlinedButton(
                            onClick = { /*TODO*/ },
                            shape = RectangleShape,
                            modifier = Modifier
                                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                                .width(290.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                Color.Magenta,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "View")
                        }
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                }
            }
        }
    }//This is the improved but not the final version which displays the product in a row but not based by their category
}

/***
 * //This is for displaying products depend on they own category but it displays it in a Column and not a Row
 *
 * @Composable
 * fun ProductView() {
 *     val context = LocalContext.current
 *     val get = CategoryFilter()
 *
 *     val groupLists = remember {
 *         mutableStateOf(listOf<List<items>>(get.favList,get.newItemList,get.unBroghtItems))
 *     }//This collects all List<items> in the system.
 *
 *
 *         LazyColumn(
 *             modifier = Modifier
 *                 .height(2500.dp)
 *                 .fillMaxWidth()
 *         ) {
 *             items(groupLists.value) {
 *
 *                 it.forEach { product->
 *
 *                     if(product.itemCategory == ""){
 *                         val lists = listOf(product.copy())
 *                         LazyRow(
 *                             modifier = Modifier.fillMaxWidth(),
 *                             verticalAlignment = Alignment.CenterVertically
 *                         ) {
 *                             items(lists){ pro ->
 *                                 Column(
 *                                     modifier = Modifier
 *                                         .fillMaxWidth()
 *                                         .padding(5.dp)
 *                                         .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
 *                                         .clip(RoundedCornerShape(12.dp)),
 *                                     verticalArrangement = Arrangement.Center,
 *                                     horizontalAlignment = Alignment.Start
 *                                 ) {
 *                                     Image(
 *                                         painter = painterResource(R.drawable.dell_latitude_5430),
 *                                         contentDescription = pro.itemName,
 *                                         modifier = Modifier.width(290.dp)
 *                                     )
 *                                     Text(
 *                                         text = pro.itemName,
 *                                         fontSize = 20.sp,
 *                                         fontWeight = FontWeight(700),
 *                                     )
 *                                     Text(text = "R. ${pro.itemPrice}")
 *                                     OutlinedButton(
 *                                         onClick = { /*TODO*/ },
 *                                         shape = RectangleShape,
 *                                         modifier = Modifier
 *                                             .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
 *                                             .width(290.dp),
 *                                         colors = ButtonColors(
 *                                             containerColor = Color.Magenta,
 *                                             contentColor = Color.White,
 *                                             disabledContentColor = Color.LightGray,
 *                                             disabledContainerColor = Color.LightGray
 *                                         )
 *                                     ) {
 *                                         Text(text = "View")
 *                                     }
 *                                 }
 *                                 Spacer(modifier = Modifier.padding(10.dp))
 *
 *                             }
 *                         }
 *                     }
 *                 }
 *             }
 *         }
 *
 *     LazyRow(
 *      modifier = Modifier.fillMaxWidth(),
 *         verticalAlignment = Alignment.CenterVertically
 *     ) {
 *         /*items(group) {
 *             Column(
 *                 modifier = Modifier
 *                     .fillMaxSize()
 *                     .padding(5.dp)
 *                     .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
 *                     .clip(RoundedCornerShape(12.dp)),
 *                 verticalArrangement = Arrangement.Center,
 *                 horizontalAlignment = Alignment.Start
 *             ) {
 *                 Image(
 *                     painter = painterResource(R.drawable.dell_latitude_5430),
 *                     contentDescription = it.itemName,
 *                     modifier = Modifier.fillMaxWidth()
 *                 )
 *                 Text(
 *                     text = it.itemName,
 *                     fontSize = 20.sp,
 *                     fontWeight = FontWeight(700),
 *                 )
 *                 Text(text = "R. ${it.itemPrice}")
 *                 OutlinedButton(
 *                     onClick = { /*TODO*/ },
 *                     shape = RectangleShape,
 *                     modifier = Modifier
 *                         .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
 *                         .width(290.dp),
 *                     colors = ButtonColors(
 *                         containerColor = Color.Magenta,
 *                         contentColor = Color.White,
 *                         disabledContentColor = Color.LightGray,
 *                         disabledContainerColor = Color.LightGray
 *                     )
 *                 ) {
 *                     Text(text = "View")
 *                 }
 *             }
 *             Spacer(modifier = Modifier.padding(10.dp))
 *
 *         }
 *         item(1) {
 *             Button(onClick = { /*TODO*/ }) {
 *                 Text(text = "View More..")
 *             }
 *         }*/
 *     }
 * }
 *
 *
 */