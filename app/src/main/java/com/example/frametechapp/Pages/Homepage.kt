package com.example.frametechapp.Pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frametechapp.Model.CategoryFilter
import com.example.frametechapp.R



var isExpanded = false
@Preview(showBackground = true)
@Composable
fun Homepage(){


    val listGetters = CategoryFilter()
    //Best Sellers
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState(), true)
        .background(Color.LightGray)
        .size(900.dp, 9000.dp)
        .padding(horizontal = 8.dp)
    ) {
        Text(text = "Best Sellers")
        LazyColumn {
            items(listGetters.FavouriteItems()) { item ->

                if (isExpanded){
                    MaxCard(
                        itemId = item.itemId,
                        itemsName = item.itemName,
                        itemDescription = item.itemDescription,
                        itemsPrice = item.itemPrice,
                        itemsImage = item.itemPhotos,
                        onClick = { /*TODO*/ }) {

                    }
                }else{
                    MiniCard(
                        itemId = item.itemId,
                        itemsName = item.itemName,
                        itemsPrice = item.itemPrice,
                        itemsImage = item.itemPhotos,
                        item.onBuy
                    )
                }

                Spacer(modifier = Modifier.padding(10.dp))

            }
        }
        //New Arrives
        Text(text = "New Arrives")
        LazyColumn {
            items(listGetters.NewArrives()) { item ->
                MiniCard(
                    itemId = item.itemId,
                    itemsName = item.itemName,
                    itemsPrice = item.itemPrice,
                    itemsImage = item.itemPhotos,
                    item.onBuy
                )
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
        //Un brought Items
        Text(text = "Un-brought Items ")
        LazyColumn {
            items(listGetters.UnBroughtItems()) { item ->
                MiniCard(
                    itemId = item.itemId,
                    itemsName = item.itemName,
                    itemsPrice = item.itemPrice,
                    itemsImage = item.itemPhotos,
                    item.onBuy
                )
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
    }
}

@Composable
fun MiniCard(
    itemId:Int,
    itemsName:String,
    itemsPrice:Double,
    itemsImage:List<Int>,
    onClick:()->Unit
){//This Method is for the miniCard in the Home Page
    val context = LocalContext.current
    var image = painterResource(R.drawable.default_image)
    val expend = remember { mutableStateOf(isExpanded) }
    for (i in itemsImage){   image = painterResource(id = i)}
    Column(
        Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Black)
            .clip(RectangleShape)
            .clickable {
                expend.value = true

                Toast
                    .makeText(context, "Expend the size of the card isExpend: $isExpanded", Toast.LENGTH_SHORT)
                    .show()
            }
            .padding(6.dp)
    ) {
        Image(painter = image, contentDescription = itemsName, modifier = Modifier.fillMaxWidth())
        Text(text = itemsName)
        Text(text = "R $itemsPrice")
        Button(onClick = {
            //Getting and sending the data to the Cart.
        }) {
            Text(text = "Add to Cart")
        }
    }
}

@Composable
fun MaxCard(
    itemId: Int,
    itemsName: String,
    itemDescription: List<String>,
    itemsPrice: Double,
    itemsImage: List<Int>,
    onClick:()->Unit,
    onCancel:() -> Unit
){//This Method is for the maxCard in the Home Page to display everything related to the item selected.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),

    ) {
        Row {
            //Two side card for the image and the description
            Sliding(image = itemsImage)
            Spacer(modifier = Modifier.padding(2.dp))
            Column {

                Text(text = itemsName)
                Text(text = itemDescription[0])
                Text(text = "R $itemsPrice")
                Button(onClick = {
                    //Getting and sending the data to the Cart.
                    onClick()
                }){
                    Text(text = "Add to Cart.")
                }
            }
       }
   }
}
@Composable
fun Sliding(image:List<Int>){//This method will be a slider for the maxCard
    var count = 0;
    var countUp = remember { mutableStateOf(count) }
    var countDown = remember { mutableStateOf(image.size-1) }
    Row(
        Modifier.fillMaxWidth(0.5f)
    ) {
        IconButton(onClick = { count += countUp.value }) {
            Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = null)
        }
        Column {
                Image(painter = painterResource(image[count]), contentDescription = null)
        }
        IconButton(onClick = { count -= countDown.value  }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
        }
    }
}