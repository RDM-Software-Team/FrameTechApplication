package com.example.frametechapp.Controller

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.frame_tech_app.Data.CartItem
import com.example.frametech_app.Data.Product
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONException
import org.json.JSONObject

class NetworkManager {
    private val client = OkHttpClient()

    fun login(email: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val formBody = FormBody.Builder()
            .add("email", email)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url("http://192.168.18.113/computer_Complex_mobile/login.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Post to main thread for error callback
                Handler(Looper.getMainLooper()).post {
                    onError("Network Error: ${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    when (response.code) {
                        200 -> {
                            val responseData = response.body?.string()
                            try {
                                val json = JSONObject(responseData ?: "")
                                if (json.has("token")) {
                                    // Post to main thread for success callback
                                    Handler(Looper.getMainLooper()).post {
                                        onSuccess(json.getString("token"))
                                    }
                                } else {
                                    // Post to main thread for error callback
                                    Handler(Looper.getMainLooper()).post {
                                        onError(json.getString("message"))
                                    }
                                }
                            } catch (e: JSONException) {
                                // Post to main thread for error callback
                                Handler(Looper.getMainLooper()).post {
                                    onError("Response Error: ${e.message}")
                                }
                            }
                        }

                        400 -> Handler(Looper.getMainLooper()).post {
                            onError("Bad Request: Check your input")
                        }
                        401 -> Handler(Looper.getMainLooper()).post {
                            onError("Unauthorized: Incorrect email or password")
                        }
                        403 -> Handler(Looper.getMainLooper()).post {
                            onError("Forbidden: Access denied")
                        }
                        500 -> Handler(Looper.getMainLooper()).post {
                            onError("Server Error: Please try again later")
                        }
                        else -> Handler(Looper.getMainLooper()).post {
                            onError("Unexpected Error: ${response.message}")
                        }
                    }
                }
            }
        })
    }

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        address: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val formBody = FormBody.Builder()
            .add("firstName", firstName)
            .add("lastName", lastName)
            .add("email", email)
            .add("phone", phone)
            .add("address", address)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url("http://192.168.18.113/computer_Complex_mobile/register.php")
            .post(formBody)
            .build()

        Log.d("NetworkManager", "Sending registration request")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("NetworkManager", "Registration request failed", e)
                onError("Network Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use { res ->
                    val responseBody = res.body?.string()
                    Log.d("NetworkManager", "Registration response: $responseBody")

                    when {
                        res.isSuccessful && responseBody?.contains("created", ignoreCase = true) == true -> {
                            Log.d("NetworkManager", "Registration successful")
                            onSuccess()
                        }
                        res.code == 400 -> onError("Bad Request: Check your input")
                        res.code == 409 -> onError("Conflict: Email already registered")
                        res.code == 500 -> onError("Server Error: Please try again later")
                        else -> onError("Unexpected Error: ${res.message}")
                    }
                }
            }
        })
    }

    fun refreshSession(token: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {//this function will be refreshing the token assigned to a user when logged in
        val formBody = FormBody.Builder()
            .add("token", token)
            .build()

        val request = Request.Builder()
            .url("http://192.168.18.113/computer_Complex_mobile/refresh_token.php")////must change to actual api url
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError("Network Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        onError("Unexpected code $response")
                        return
                    }
                    val responseData = response.body?.string()
                    val json = JSONObject(responseData ?: "")
                    if (json.has("token")) {
                        onSuccess(json.getString("token"))
                    } else {
                        onError(json.getString("message"))
                    }
                }
            }
        })
    }
    fun addToCart(
        token: String,
        productId: Int,
        quantity: Int,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val formBody = FormBody.Builder()
            .add("token", token)
            .add("product_id", productId.toString())
            .add("quantity", quantity.toString())
            .build()

        val request = Request.Builder()
            .url("http://192.168.18.113/computer_Complex_mobile/cart.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError("Network Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    when (response.code) {
                        200 -> onSuccess("Cart updated successfully")
                        401 -> onError("Unauthorized: Invalid or expired token")
                        else -> onError("Unexpected Error: ${response.message}")
                    }
                }
            }
        })
    }
    fun fetchCartItems(
        token: String,
        page: Int,
        onSuccess: (List<CartItem>, Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        val formBody = FormBody.Builder()
            .add("token", token)
            .add("page", page.toString())
            .build()

        val request = Request.Builder()
            .url("http://192.168.18.113/computer_Complex_mobile/fetch_cart.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError("Network Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        onError("Unexpected Error: ${response.message}")
                        return
                    }
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        try {
                            val json = JSONObject(responseData)
                            val itemsArray = json.getJSONArray("items")
                            val cartItems = mutableListOf<CartItem>()
                            val morePages = json.getBoolean("more_pages")

                            for (i in 0 until itemsArray.length()) {
                                val item = itemsArray.getJSONObject(i)
                                val cartItem = CartItem(
                                    itemId = item.getInt("item_id"),
                                    itemName = item.getString("item_name"),
                                    itemPrice = item.getDouble("item_price"),
                                    quantity = item.getInt("quantity")
                                )
                                cartItems.add(cartItem)
                            }
                            onSuccess(cartItems, morePages)
                        } catch (e: Exception) {
                            onError("Parsing Error: ${e.message}")
                        }
                    } else {
                        onError("No data received")
                    }
                }
            }
        })
    }

    fun updateCartItem(
        token: String,
        cartItemId: Int,
        quantity: Int,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val formBody = FormBody.Builder()
            .add("token", token)
            .add("cart_item_id", cartItemId.toString())
            .add("quantity", quantity.toString())
            .build()

        val request = Request.Builder()
            .url("http://192.168.18.113/computer_Complex_mobile/update_cart_item.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError("Network Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        onSuccess("Item updated successfully")
                    } else {
                        onError("Failed to update item: ${response.message}")
                    }
                }
            }
        })
    }

    fun deleteCartItem(
        token: String,
        cartItemId: Int,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val formBody = FormBody.Builder()
            .add("token", token)
            .add("cart_item_id", cartItemId.toString())
            .build()

        val request = Request.Builder()
            .url("http://192.168.18.113/computer_Complex_mobile/delete_cart_item.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError("Network Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        onSuccess("Item deleted successfully")
                    } else {
                        onError("Failed to delete item: ${response.message}")
                    }
                }
            }
        })
    }

    fun fetchProducts(
        token: String,
        category: String,
        page: Int,
        limit: Int,
        onSuccess: (List<Product>, Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        val formBody = FormBody.Builder()
            .add("token", token)
            .add("category", category)
            .add("page", page.toString())
            .add("limit", limit.toString())
            .build()

        val request = Request.Builder()
            .url("http://192.168.18.113/computer_Complex_mobile/fetch_products.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    onError("Network Error: ${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Handler(Looper.getMainLooper()).post {
                            onError("Unexpected Error: ${response.message}")
                        }
                        return
                    }
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        try {
                            val json = JSONObject(responseData)
                            val productsArray = json.getJSONArray("products")
                            val products = mutableListOf<Product>()
                            val morePages = json.getBoolean("more_pages")

                            for (i in 0 until productsArray.length()) {
                                val product = productsArray.getJSONObject(i)
                                val productItem = Product(
                                    productId = product.getInt("product_id"),
                                    pName = product.getString("pName"),
                                    description = product.getString("description"),
                                    price = product.getDouble("price"),
                                    category = product.getString("category"),
                                    images = product.optString("images", null)
                                )
                                products.add(productItem)
                            }
                            Handler(Looper.getMainLooper()).post {
                                onSuccess(products, morePages)
                            }
                        } catch (e: JSONException) {
                            Handler(Looper.getMainLooper()).post {
                                onError("Parsing Error: ${e.message}")
                            }
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            onError("No data received")
                        }
                    }
                }
            }
        })
    }
}


