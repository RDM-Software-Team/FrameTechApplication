package com.example.frametechapp.Controller

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import com.example.frame_tech_app.Data.CartItem
import com.example.frametech_app.Data.Category
import com.example.frametech_app.Data.Product
import com.example.frametechapp.IPConfigurer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    val ipaddress = IPConfigurer()

    private val  baseUrl ="http://192.168.18.113/computer_Complex_mobile"
    private val client = OkHttpClient()
    fun login(email: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val formBody = FormBody.Builder()
            .add("email", email)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/login.php")
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
                    val responseBody = response.body?.string()
                    if (response.isSuccessful) {
                        try {
                            val json = JSONObject(responseBody ?: "")
                            if (json.has("token")) {
                                Handler(Looper.getMainLooper()).post {
                                    onSuccess(json.getString("token"))
                                }
                            } else {
                                Handler(Looper.getMainLooper()).post {
                                    onError(json.getString("message"))
                                }
                            }
                        } catch (e: JSONException) {
                            Handler(Looper.getMainLooper()).post {
                                onError("Response Error: ${e.message}")
                            }
                        }
                    } else {
                        val errorMessage = when (response.code) {
                            400 -> "Bad Request: Check your input"
                            401 -> "Unauthorized: Incorrect email or password"
                            403 -> "Forbidden: Access denied"
                            500 -> "Server Error: Please try again later"
                            else -> "Unexpected Error: ${response.message}"
                        }
                        Handler(Looper.getMainLooper()).post {
                            onError(errorMessage)
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
            .url("$baseUrl/register.php")
            .post(formBody)
            .build()

        Log.d("NetworkManager", "Sending registration request")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("NetworkManager", "Registration request failed", e)
                Handler(Looper.getMainLooper()).post {
                    onError("Network Error: ${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val responseBody = response.body?.string()
                    Log.d("NetworkManager", "Registration response: $responseBody")

                    val errorMessage = when {
                        response.isSuccessful && responseBody?.contains("created", ignoreCase = true) == true -> {
                            Log.d("NetworkManager", "Registration successful")
                            Handler(Looper.getMainLooper()).post {
                                onSuccess()
                            }
                            return
                        }
                        response.code == 400 -> "Bad Request: Check your input"
                        response.code == 409 -> "Conflict: Email already registered"
                        response.code == 500 -> "Server Error: Please try again later"
                        else -> "Unexpected Error: ${response.message}"
                    }
                    Handler(Looper.getMainLooper()).post {
                        onError(errorMessage)
                    }
                }
            }
        })
    }
    fun refreshToken(token: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val formBody = FormBody.Builder()
            .add("token", token)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/refresh_token.php")
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
                    val responseBody = response.body?.string()
                    if (response.isSuccessful) {
                        try {
                            val json = JSONObject(responseBody ?: "")
                            if (json.has("token")) {
                                Handler(Looper.getMainLooper()).post {
                                    onSuccess(json.getString("token"))
                                }
                            } else {
                                Handler(Looper.getMainLooper()).post {
                                    onError(json.getString("message"))
                                }
                            }
                        } catch (e: JSONException) {
                            Handler(Looper.getMainLooper()).post {
                                onError("Response Error: ${e.message}")
                            }
                        }
                    } else {
                        val errorMessage = when (response.code) {
                            400 -> "Bad Request: Check your input"
                            401 -> "Unauthorized: Invalid token"
                            500 -> "Server Error: Please try again later"
                            else -> "Unexpected Error: ${response.message}"
                        }
                        Handler(Looper.getMainLooper()).post {
                            onError(errorMessage)
                        }
                    }
                }
            }
        })
    }
    fun refreshSession(token: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val formBody = FormBody.Builder()
            .add("token", token)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/refresh_token.php")
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
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        try {
                            val json = JSONObject(responseBody ?: "")
                            if (json.has("token")) {
                                Handler(Looper.getMainLooper()).post {
                                    onSuccess(json.getString("token"))
                                }
                            } else {
                                Handler(Looper.getMainLooper()).post {
                                    onError(json.getString("message"))
                                }
                            }
                        } catch (e: JSONException) {
                            Handler(Looper.getMainLooper()).post {
                                onError("Response Error: ${e.message}")
                            }
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            onError("Unexpected code ${response.code}")
                        }
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
            .url("$baseUrl/cart.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    onError("Network Error: ${e.message}")
                    Log.d("Network Error: ", "${ e.message }")

                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val errorMessage = when {
                        response.isSuccessful -> "Cart updated successfully"
                        response.code == 401 -> "Unauthorized: Invalid or expired token"
                        else -> "Unexpected Error: ${response.message}"
                    }
                    Handler(Looper.getMainLooper()).post {
                        if (response.isSuccessful) {
                            onSuccess(errorMessage)
                            Log.d("Successful: ",errorMessage)
                        } else {
                            onError(errorMessage)
                            Log.d("Failed: ",errorMessage)

                        }
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
            .url("$baseUrl/fetch_cart.php")
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
                            Log.d("Unexpected Error: ", response.message)
                        }
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
                                    productId = item.getInt("product_id"),
                                    quantity = item.getInt("quantity"),
                                    cartId = item.getInt("cart_id"),
                                    cartCreated = item.getString("cart_created"),
                                    status = item.getString("status")
                                )
                                cartItems.add(cartItem)
                                Log.d("Saved items: ", "$cartItem")
                            }
                            Handler(Looper.getMainLooper()).post {
                                onSuccess(cartItems, morePages)
                                Log.d("Successful items and pages: ", "$cartItems, $morePages")
                            }
                        } catch (e: Exception) {
                            Handler(Looper.getMainLooper()).post {
                                onError("Parsing Error: ${e.message}")
                                Log.e("Parsing Error: ", "${e.message}")
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
            .url("$baseUrl/update_cart.php")
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
                    val errorMessage = when {
                        response.isSuccessful -> "Cart item updated successfully"
                        response.code == 401 -> "Unauthorized: Invalid or expired token"
                        else -> "Unexpected Error: ${response.message}"
                    }
                    Handler(Looper.getMainLooper()).post {
                        if (response.isSuccessful) {
                            onSuccess(errorMessage)
                        } else {
                            onError(errorMessage)
                        }
                    }
                }
            }
        })
    }

    fun removeFromCart(
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
            .url("$baseUrl/remove_cart.php")
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
                    val errorMessage = when {
                        response.isSuccessful -> "Item removed from cart successfully"
                        response.code == 401 -> "Unauthorized: Invalid or expired token"
                        else -> "Unexpected Error: ${response.message}"
                    }
                    Handler(Looper.getMainLooper()).post {
                        if (response.isSuccessful) {
                            onSuccess(errorMessage)
                        } else {
                            onError(errorMessage)
                        }
                    }
                }
            }
        })
    }
    suspend fun fetchCategories(): Result<List<Category>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/fetch_categories.php")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val responseBody = response.body?.string() ?: throw IOException("Empty response body")
                val jsonObject = JSONObject(responseBody)
                val categoriesArray = jsonObject.getJSONArray("categories")
                val categories = List(categoriesArray.length()) { i ->
                    Category(categoriesArray.getString(i))
                }
                Log.d("fetch categories response: ","$categories")
                Result.success(categories)
            }
        } catch (e: Exception) {
            Log.d("fetch categories errors: ","${e.message}")

            Result.failure(e)
        }
    }

    suspend fun fetchProducts(token: String, category: String, page: Int, limit: Int): Result<Pair<List<Product>, Boolean>> = withContext(Dispatchers.IO) {
        try {
            val requestBody = FormBody.Builder()
                .add("token", token)
                .add("category", category)
                .add("page", page.toString())
                .add("limit", limit.toString())
                .build()

            val request = Request.Builder()
                .url("$baseUrl/fetching_products.php")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val responseBody = response.body?.string() ?: throw IOException("Empty response body")

                // Log the raw response to debug
                Log.e("Raw Response", responseBody)

                // Check if the response is valid JSON
                val jsonObject = try {
                    JSONObject(responseBody)
                } catch (e: JSONException) {
                    throw IOException("Invalid JSON format: ${e.message}")
                }

                val productsArray = jsonObject.getJSONArray("products")
                val morePages = jsonObject.getBoolean("more_pages")
                val products = List(productsArray.length()) { i ->
                    val productJson = productsArray.getJSONObject(i)
                    Product(
                        productId = productJson.getInt("product_id"),
                        pName = productJson.getString("pName"),
                        description = productJson.getString("discription"),
                        price = productJson.getDouble("price"),
                        category = productJson.getString("category"),
                        imagePath = productJson.optString("image_path")
                    )
                }

                Result.success(Pair(products, morePages))
            }
        } catch (e: Exception) {
            Log.e("fetch products error: ", e.message ?: "Unknown error")
            Result.failure(e)
        }
    }

    //reading Longblob image
    fun decodeBase64ToBitmap(base64: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(base64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}


