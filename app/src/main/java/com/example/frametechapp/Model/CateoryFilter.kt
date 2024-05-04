package com.example.frametechapp.Model

import com.example.frametech_app.Data.items
import java.sql.Timestamp


class CateoryFilter {
    //Lists that will be containing items for the three category
    val favList = listOf(
        items(
            itemId = 1090,
            itemName = "kl",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 4321,
            itemName = "bnm,",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 9086,
            itemName = "hjklp",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 1237,
            itemName = "jklp",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 7908,
            itemName = "hjkl;[",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        )
    )

    val newItemList = listOf(
        items(
            itemId = 2589,
            itemName = "dfghu",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 9800,
            itemName = "hjkop",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 9878,
            itemName = "bjkl;'",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 2512,
            itemName = "hjiop",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 9876,
            itemName = "hjkop[",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
    )

    val unBroghtItems = listOf(
        items(
            itemId = 8865,
            itemName = "hjiop[",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 2589,
            itemName = "bhjkop[]",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 9976,
            itemName = "hjkl",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 2467,
            itemName = "jkl;",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 6878,
            itemName = "fsassdfdsa",
            itemPhotos = emptyList(),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 0.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
    )
    //The following methods/Function will be used to filter through data to create categories in the homepage and etc.
    //Looks for the Favourite items brought the most and favoured by the customers.
    fun FavouriteItems():List<items>{// The data will be taken from the database , (from 02/05/2024 to 17/05/2024 will be taken from memory)
        return favList;
    }
    //Looks for the New Items added the system.
    fun NewArrives():List<items>{

        return newItemList;
    }
    //Looks for item that are not as  brought as the rest to try push them to the customers.
    fun UnBroughtItems():List<items>{

        return unBroghtItems;
    }

}
