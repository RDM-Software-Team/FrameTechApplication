package com.example.frametechapp.Pages

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
            .fillMaxWidth()
            .fillMaxHeight()
            .border(1.dp, Color.Black)
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome to Sell to Us for you",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .border(1.dp, Color.Transparent)
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

        if (buttonMakeSell) {
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text(text = "Enter Product Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = productDescription,
                onValueChange = { productDescription = it },
                label = { Text(text = "Enter Product Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            LazyRow {
                item {
                    Column(
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp)
                            .padding(16.dp)
                            .border(1.dp, Color.Black),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (imageBackUri == null) {

                            IconButton(
                                onClick = { launcherBack.launch("image/*") },
                                modifier = Modifier
                                    .border(1.dp, Color.Transparent)
                                    .width(120.dp)
                                    .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
                            ) {
                                Row(
                                    modifier = Modifier.width(500.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Sharp.Add,
                                        contentDescription = null
                                    )
                                    Text(text = "Back Photo")
                                }
                            }
                        } else {
                            Image(
                                painter = if(imageBackUri != null) rememberAsyncImagePainter(imageBackUri) else painterResource(R.drawable.default_image),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        //Changing the image
                                        launcherBack.launch("image/*")
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(10.dp))
                }


                //Front Image
                item {
                    Column(
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp)
                            .padding(16.dp)
                            .border(1.dp, Color.Black),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (imageFrontUri == null) {

                            IconButton(
                                onClick = { launcherFront.launch("image/*") },
                                modifier = Modifier
                                    .border(1.dp, Color.Transparent)
                                    .width(120.dp)
                                    .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
                            ) {
                                Row(
                                    modifier = Modifier.width(200.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Sharp.Add,
                                        contentDescription = null
                                    )
                                    Text(text = "Front Photo")
                                }
                            }
                        } else {
                            Image(
                                painter = if(imageFrontUri != null) rememberAsyncImagePainter(imageFrontUri) else painterResource(R.drawable.default_image),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        //Changing the image
                                        launcherFront.launch("image/*")
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(10.dp))
                }

                item {
                    //Aril Photo

                    Column(
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp)
                            .padding(16.dp)
                            .border(1.dp, Color.Black),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (imageArielUri == null) {

                            IconButton(
                                onClick = { launcherAriel.launch("image/*") },
                                modifier = Modifier
                                    .border(1.dp, Color.Transparent)
                                    .width(120.dp)
                                    .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
                            ) {
                                Row(
                                    modifier = Modifier.width(200.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Sharp.Add,
                                        contentDescription = null
                                    )
                                    Text(text = "Ariel Photo")
                                }
                            }
                        } else {
                            Image(
                                painter = if(imageArielUri != null) rememberAsyncImagePainter(imageArielUri) else painterResource(R.drawable.default_image),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        //Changing the image
                                        launcherAriel.launch("image/*")
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(10.dp))
                }
            }

            OutlinedButton(
                onClick = {
                    if (imageBackUri == null || imageFrontUri == null || imageArielUri == null) {
                        Toast.makeText(context, "Can't save empty images", Toast.LENGTH_SHORT).show()
                        sellReports.add(
                            SellsReports(
                                productName = productName,
                                productDescription = productDescription,
                            )
                        )
                        productName = ""
                        productDescription = ""
                        buttonMakeSell = false
                    } else {
                        sellReports.add(
                            SellsReports(
                                productName = productName,
                                productDescription = productDescription,
                                productImages = listOf(
                                    imageBackUri,
                                    imageFrontUri,
                                    imageArielUri
                                )
                            )
                        )
                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                        buttonMakeSell = false
                        productName = ""
                        productDescription = ""
                    }
                },
                modifier = Modifier
                    .width(160.dp)
                    .padding(top = 10.dp),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Green)
            ) {
                Text(text = "Send", color = Color.White)
            }
        } else {
            OutlinedButton(
                onClick = { buttonMakeSell = true },
                modifier = Modifier
                    .width(160.dp)
                    .padding(top = 10.dp),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Blue)
            ) {
                Text(text = "Make a Sell", color = Color.White)
            }
        }
        SellProcess(sellReports)
    }
}

@Composable
fun SellProcess(query: MutableList<SellsReports> = mutableListOf()) {
    val process by remember {
        mutableStateOf(listOf("processing", "reviewing", "approved", "denied"))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Image", modifier = Modifier
                .width(105.dp)
                .border(1.dp, Color.Black))
            Text(text = "Name", modifier = Modifier
                .width(105.dp)
                .border(1.dp, Color.Black))
            Text(text = "Description", modifier = Modifier
                .width(105.dp)
                .border(1.dp, Color.Black))
            Text(text = "Status",
                 modifier = Modifier
                     .width(105.dp)
                     .border(1.dp, Color.Black))
        }

        if (query.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(10.dp))
            query.forEach { selling ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val ImageUri = selling.productImages.getOrNull(0) // Assuming front image URI is at index 1
                    Image(
                        painter = if (ImageUri != null) rememberAsyncImagePainter(ImageUri)
                        else painterResource(R.drawable.default_image),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .width(105.dp)
                            .border(1.dp, Color.Black)
                    )
                    Text(text = selling.productName, modifier = Modifier
                        .width(105.dp)
                        .height(105.dp)
                        .border(1.dp, Color.Black)
                        .padding(10.dp)
                    )
                    Text(text = selling.productDescription, modifier = Modifier
                        .width(105.dp)
                        .height(105.dp)
                        .border(1.dp, Color.Black)
                        .padding(10.dp)
                    )
                    Text(text = process[0],
                         fontSize = 10.sp
                        ,modifier = Modifier
                            .width(105.dp)
                            .height(105.dp)
                            .border(1.dp, Color.Black)
                            .padding(10.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            Text(
                text = "No product is being sold to us :)",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(850.dp)
                    .padding(top = 20.dp)
            )
        }
    }
}
