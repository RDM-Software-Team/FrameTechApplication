package com.example.frametechapp.Controller

import com.example.frametech_app.Data.items
import com.example.frametechapp.R
import java.sql.Timestamp


class CategoryFilter {
    //Lists that will be containing items for the three category
    var favList = listOf(
        items(
            itemId = 1090,
            itemName = "Dell optiplex",
            itemPhotos = listOf(R.drawable.dell_optiplex_7010_mff_desktop_pc),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 12999.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 4321,
            itemName = "Dell latitude",
            itemPhotos = listOf(R.drawable.dell_latitude_5430),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 10900.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 9086,
            itemName = "Dell Vostro ",
            itemPhotos = listOf(R.drawable.dell_vostro_3520),
            itemDescription = emptyList(),
            itemCategory = "CellPhone",
            itemPrice = 6500.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 1237,
            itemName = "Dell Inspiron",
            itemPhotos = listOf(R.drawable.dell_inspiron_3520),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 10900.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 7908,
            itemName = "Dell Optiplex",
            itemPhotos = listOf(R.drawable.dell_optiplex_7410_aio_desktop_pc),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 20000.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        )
    )

    val newItemList = listOf(
        items(
            itemId = 2589,
            itemName = "Hp Mouse",
            itemPhotos = listOf(R.drawable.hp_wireless_mouse_200),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 2094.90,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 9800,
            itemName = "Hp Laptop",
            itemPhotos = listOf(R.drawable.hp_laptop_15_fc0001ni___ryzen_part3),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 19200.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 9878,
            itemName = "Hp Usb Mouse",
            itemPhotos = listOf(R.drawable.hp_usb_premium_mouse),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 650.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 2512,
            itemName = "All in One",
            itemPhotos = listOf(R.drawable.hp_all_in_one_27_cr0003ni_pc__8j942ea_),
            itemDescription = emptyList(),
            itemCategory = "Desktop",
            itemPrice = 12000.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 9876,
            itemName = "Wired keyboard",
            itemPhotos = listOf(R.drawable.microsoft_wired_desktop_keyboard_600),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 1200.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
    )

    val unBroghtItems = listOf(
        items(
            itemId = 8865,
            itemName = "Gaming Keyboard",
            itemPhotos = listOf(R.drawable.redragon_rd_k506_centaur_membrane_rainbow_led_backlit_wired_gaming_keyboard),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 900.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 2589,
            itemName = "All in One",
            itemPhotos = listOf(R.drawable.hp_all_in_one_27_cr0003ni_pc__8j942ea_),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 29119.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 9976,
            itemName = "Hp Laptop ",
            itemPhotos = listOf(R.drawable.hp_laptop_15_fc0001ni___ryzen),
            itemDescription = emptyList(),
            itemCategory = "",
            itemPrice = 10000.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 2467,
            itemName = "Dell optiplex 7410",
            itemPhotos = listOf(R.drawable.dell_optiplex_7410_aio_desktop_pc),
            itemDescription = emptyList(),
            itemCategory = "Laptops",
            itemPrice = 7000.0,
            dateAdded = Timestamp(System.currentTimeMillis()),
            onBuy = {},
            onCancel = {}
        ),
        items(
            itemId = 6878,
            itemName = "Hp Laptop 15",
            itemPhotos = listOf(R.drawable.hp_laptop_15_fc0001ni___ryzen),
            itemDescription = emptyList(),
            itemCategory = "Laptops",
            itemPrice = 21000.0,
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
