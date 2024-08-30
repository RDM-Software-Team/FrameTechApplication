package com.example.frametechapp.Pages

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

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
//    val searchValue = remember { mutableStateOf("") }
//    val get = CategoryFilter()
// Column(
//  modifier = Modifier
//      .verticalScroll(rememberScrollState(), enabled = true)
//      .background(Color.LightGray)
//      .size(1200.dp, 4500.dp)
//      .padding(horizontal = 8.dp)
// ) {
//  //top options to search and filter
//
//  Row(
//   modifier = Modifier.width(900.dp),
//   verticalAlignment = Alignment.CenterVertically,
//   horizontalArrangement = Arrangement.SpaceBetween
//  ){
//   TextField(
//    value = searchValue.value,
//    onValueChange = { searchValue.value = it },
//    leadingIcon = {
//     IconButton(onClick = {}) {
//      Icon(Icons.Filled.Search, contentDescription = "Icon")//Need to make a working search function/action..
//     }
//    },
//    maxLines = 2,
//    modifier = Modifier
//        .width(220.dp)
//        .height(50.dp)
//   )
//
//   Button(onClick = { /*TODO*/ },
//    ) {
//    Icon(imageVector = Icons.AutoMirrored.Filled.Sort, contentDescription = null)
//    Text(text = "Filter")
//   }
//  }
//     HorizontalDivider(
//         Modifier
//             .background(Color.Black)
//             .padding(2.dp)
//     )
//  //Bottom view to display the items..
//   ProductView()
// }
}
@Composable
fun ProductView() {
//    val context = LocalContext.current
//    val get = CategoryFilter()
//
//    val groupLists = remember {
//        mutableStateOf(listOf<List<items>>(get.favList,get.newItemList,get.unBroghtItems))
//    }//This collects all List<items> in the system.
//
//
//    Column {
//        groupLists.value.forEach { itemList ->
//            Text(
//                text = itemList.firstOrNull()?.itemCategory ?: "New ",
//                style = TextStyle(fontWeight = FontWeight.Bold),
//                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//            )
//            LazyRow(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                items(itemList) { product ->
//                    Column(
//                        modifier = Modifier
//                            .padding(5.dp)
//                            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
//                            .clip(RoundedCornerShape(12.dp)),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.Start
//                    ) {
//                        Image(
//                            painter = painterResource(R.drawable.dell_latitude_5430),
//                            contentDescription = product.itemName,
//                            modifier = Modifier.width(290.dp)
//                        )
//                        Text(
//                            text = product.itemName,
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight(700),
//                        )
//                        Text(text = "R. ${product.itemPrice}")
//                        OutlinedButton(
//                            onClick = { /*TODO*/ },
//                            shape = RectangleShape,
//                            modifier = Modifier
//                                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
//                                .width(290.dp),
//                            colors = ButtonDefaults.outlinedButtonColors(
//                                Color.Magenta,
//                                contentColor = Color.White
//                            )
//                        ) {
//                            Text(text = "View")
//                        }
//                    }
//                    Spacer(modifier = Modifier.padding(10.dp))
//                }
//            }
//        }
//    }//This is the improved but not the final version which displays the product in a row but not based by their category
}

