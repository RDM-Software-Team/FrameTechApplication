package com.example.frametechapp.Controller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.frame_tech_app.Data.CartItem
import com.example.frametech_app.Data.Category
import com.example.frametech_app.Data.Product
import com.example.frametechapp.Data.RepairRequest
import com.example.frametechapp.Data.SellListing
import com.example.frametechapp.IPConfigurer
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
}
class NetworkManager {
    val ipaddress = IPConfigurer()
    private val gson = Gson()

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
    // Verify session function
    fun verifySession(token: String, onSuccess: (Boolean) -> Unit, onError: (String) -> Unit) {
        val formBody = FormBody.Builder()
            .add("token", token)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/verify_session.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainThreadCallback { onError("Network Error: ${e.message}") }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val json = JSONObject(responseBody ?: "")
                        val isValid = json.optString("message") == "Session valid"
                        mainThreadCallback { onSuccess(isValid) }
                    } else {
                        mainThreadCallback { onError("Unexpected error: ${response.message}") }
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
        callback: (Result<CartResponse>) -> Unit
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
                callback(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        callback(Result.failure(IOException("Unexpected code $response")))
                        return
                    }

                    val responseBody = response.body?.string()
                    if (responseBody == null) {
                        callback(Result.failure(IOException("Empty response body")))
                        return
                    }

                    try {
                        val jsonObject = JSONObject(responseBody)
                        val items = jsonObject.getJSONArray("items")
                        val morePages = jsonObject.getBoolean("more_pages")

                        // Parse items into your data class
                        val cartItems = parseCartItems(items)

                        callback(Result.success(CartResponse(cartItems, morePages)))
                    } catch (e: Exception) {
                        callback(Result.failure(e))
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
            .url("$baseUrl/update_cart_item.php")
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

    suspend fun removeFromCart(token: String, cartItemId: Int): NetworkResult<String> = withContext(Dispatchers.IO) {
        try {
            val formBody = FormBody.Builder()
                .add("token", token)
                .add("cart_item_id", cartItemId.toString())
                .build()

            val request = Request.Builder()
                .url("$baseUrl/delete_cart_item.php")
                .post(formBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext NetworkResult.Error("Unexpected code ${response.code}")
                }

                val responseBody = response.body?.string()
                if (responseBody == null) {
                    return@withContext NetworkResult.Error("Empty response body")
                }

                val jsonResponse = JSONObject(responseBody)
                val message = jsonResponse.optString("message", "Unknown response")

                return@withContext NetworkResult.Success(message)
            }
        } catch (e: Exception) {
            return@withContext NetworkResult.Error(e.message ?: "Unknown error occurred")
        }
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
    fun requestRepair(
        context: Context,
        token: String,
        description: String,
        bookedDate: String,
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            inputStream?.use { stream ->
                val buffer = stream.readBytes()
                if (buffer.size > 10_000_000) {
                    onError("Image exceeds the 10MB limit.")
                    return
                }

                val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"
                val fileExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"
                val fileName = "image.$fileExtension"

                val formBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("token", token)
                    .addFormDataPart("description", description)
                    .addFormDataPart("booked_date", bookedDate)
                    .addFormDataPart("image", fileName,
                        buffer.toRequestBody(mimeType.toMediaTypeOrNull(), 0,buffer.size)
                    )
                    .build()

                val request = Request.Builder()
                    .url("$baseUrl/request_repair.php")
                    .post(formBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        mainThreadCallback { onError("Network Error: ${e.message}") }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            val responseBody = response.body?.string()
                            if (response.isSuccessful && responseBody != null) {
                                mainThreadCallback { onSuccess(responseBody) }
                                Log.d("Successful request repair","$responseBody")
                            } else {
                                mainThreadCallback { onError("Error: ${response.message}") }
                            }
                        }
                    }
                })
            } ?: mainThreadCallback { onError("Failed to open input stream for the image") }
        } catch (e: Exception) {
            mainThreadCallback { onError("Error processing image: ${e.message}") }
        }
    }

    // Function to sell items
    fun sellItems(
        context: Context,
        token: String,
        description: String,
        price: String,
        imageUris: List<Uri>,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (imageUris.size != 3) {
            mainThreadCallback { onError("Please provide 3 images for selling.") }
            return
        }

        try {
            val formBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("description", description)
                .addFormDataPart("price", price)

            for ((index, imageUri) in imageUris.withIndex()) {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                inputStream?.use { stream ->
                    val buffer = stream.readBytes()
                    if (buffer.size > 10_000_000) {
                        mainThreadCallback { onError("Image ${index + 1} exceeds the 10MB limit.") }
                        return
                    }

                    val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"
                    val fileExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"
                    val fileName = "image${index + 1}.$fileExtension"

                    formBody.addFormDataPart("image${index + 1}", fileName,
                        buffer.toRequestBody(mimeType.toMediaTypeOrNull(), 0, buffer.size)
                    )
                } ?: run {
                    mainThreadCallback { onError("Failed to open input stream for image ${index + 1}") }
                    return
                }
            }

            val request = Request.Builder()
                .url("$baseUrl/sell_item.php")
                .post(formBody.build())
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    mainThreadCallback { onError("Network Error: ${e.message}") }
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        val responseBody = response.body?.string()
                        if (response.isSuccessful && responseBody != null) {
                            mainThreadCallback { onSuccess(responseBody) }
                            Log.d("Successful "," $responseBody")
                        } else {
                            mainThreadCallback { onError("Error: ${response.message}") }

                            Log.e("Error in the Network class"," ${response.message}")
                        }
                    }
                }
            })
        } catch (e: Exception) {
            mainThreadCallback { onError("Error processing images: ${e.message}") }
        }
    }

    // Function to view repair requests
    fun viewRepairRequests(token: String, onSuccess: (List<RepairRequest>) -> Unit, onError: (String) -> Unit) {
        val formBody = FormBody.Builder()
            .add("token", token)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/view_repair_requests.php")
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
                    if (response.isSuccessful && responseBody != null) {
                        try {
                            val repairRequests = parseRepairRequests(responseBody)
                            Handler(Looper.getMainLooper()).post {
                                onSuccess(repairRequests)
                            }
                        } catch (e: JSONException) {
                            Handler(Looper.getMainLooper()).post {
                                onError("Invalid response format: ${e.message}\nResponse: $responseBody")
                                Log.e("Invalid response format: ","${e.message}\n" +
                                        "Response: $responseBody")
                            }
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            onError("Error: ${response.message}\nResponse: $responseBody")
                            Log.e("Invalid response format: ","${response.message}\n" +
                                    "Response: $responseBody")
                        }
                    }
                }
            }
        })
    }


    // Function to view sell listings
    fun viewSellListings(token: String, onSuccess: (List<SellListing>) -> Unit, onError: (String) -> Unit) {
        val formBody = FormBody.Builder()
            .add("token", token)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/view_sell_listings.php")
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
                    if (response.isSuccessful && responseBody != null) {
                        try {
                            val sellListings = parseSellListings(responseBody)
                            Handler(Looper.getMainLooper()).post {
                                onSuccess(sellListings)
                            }
                        } catch (e: JSONException) {
                            Handler(Looper.getMainLooper()).post {
                                onError("Invalid response format: ${e.message}\nResponse: $responseBody")
                                Log.e("Invalid response format", "${e.message}\nResponse: $responseBody")
                            }
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            onError("Error: ${response.message}\nResponse: $responseBody")
                            Log.e("Error", "${response.message}\nResponse: $responseBody")
                        }
                    }
                }
            }
        })
    }
    private fun parseRepairRequests(responseBody: String): List<RepairRequest> {
        val repairRequests = mutableListOf<RepairRequest>()
        val trimmedResponse = responseBody.trim()
        if (trimmedResponse.startsWith("[") && trimmedResponse.endsWith("]")) {
            val jsonArray = JSONArray(trimmedResponse)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val repairRequest = RepairRequest(
                    repairId = jsonObject.getInt("repair_id"),
                    problemDescription = jsonObject.getString("description"),
                    bookedDate = jsonObject.getString("booked_date"),
                    profileImage = jsonObject.getString("image")
                )
                repairRequests.add(repairRequest)
            }
        } else {
            throw JSONException("Response is not a valid JSON array")
        }
        return repairRequests
    }

    private fun parseSellListings(responseBody: String): List<SellListing> {
        val sellListings = mutableListOf<SellListing>()
        val jsonArray = JSONArray(responseBody)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val sellListing = SellListing(
                sellId = jsonObject.getInt("sell_id"),
                description = jsonObject.getString("description"),
                price = jsonObject.getDouble("price"),
                image1Base64 = jsonObject.getString("image1"),
                image2Base64 = jsonObject.getString("image2"),
                image3Base64 = jsonObject.getString("image3")
            )
            sellListings.add(sellListing)
        }
        return sellListings
    }

    private fun parseCartItems(jsonArray: JSONArray): List<CartItem> {
        val cartItems = mutableListOf<CartItem>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)

            val cartItem = CartItem(
                itemId = jsonObject.getInt("item_id"),
                productId = jsonObject.getInt("product_id"),
                quantity = jsonObject.getInt("quantity"),
                cartId = jsonObject.getInt("cart_id"),
                cartCreated = jsonObject.getString("cart_created"),
                status = jsonObject.getString("status"),
                productName = jsonObject.getString("pName"),
                productDescription = jsonObject.getString("discription"),
                productImage = jsonObject.getString("image_path"),
                productPrice = jsonObject.getDouble("price")
            )

            cartItems.add(cartItem)
        }

        return cartItems
    }
    fun createOrder(token: String, onSuccess: (String, Double) -> Unit, onError: (String) -> Unit) {
        val formBody = FormBody.Builder()
            .add("token", token)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/createOrder.php")  // Your API URL
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
                    if (response.isSuccessful && responseBody != null) {
                        try {
                            val json = JSONObject(responseBody)
                            if (json.getBoolean("success")) {
                                val orderId = json.getString("order_id")
                                val totalPrice = json.getDouble("totalPrice")
                                Handler(Looper.getMainLooper()).post {
                                    onSuccess(orderId, totalPrice)
                                }
                            } else {
                                Handler(Looper.getMainLooper()).post {
                                    onError("Order creation failed: ${json.getString("message")}")
                                }
                            }
                        } catch (e: JSONException) {
                            Handler(Looper.getMainLooper()).post {
                                onError("Invalid response format: ${e.message}\nResponse: $responseBody")
                                Log.e("Invalid response format", "${e.message}\nResponse: $responseBody")
                            }
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            onError("Error: ${response.message}\nResponse: $responseBody")
                            Log.e("Error", "${response.message}\nResponse: $responseBody")
                        }
                    }
                }
            }
        })
    }

    fun processPayment(token: String, paymentType: String, orderId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val formBody = FormBody.Builder()
            .add("token", token)
            .add("payment_type", paymentType)
            .add("order_id", orderId)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/processPayment.php")  // Your API URL
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
                    if (response.isSuccessful && responseBody != null) {
                        try {
                            val json = JSONObject(responseBody)
                            if (json.getBoolean("success")) {
                                Handler(Looper.getMainLooper()).post {
                                    onSuccess()
                                }
                            } else {
                                Handler(Looper.getMainLooper()).post {
                                    onError("Payment processing failed: ${json.getString("message")}")
                                }
                            }
                        } catch (e: JSONException) {
                            Handler(Looper.getMainLooper()).post {
                                onError("Invalid response format: ${e.message}\nResponse: $responseBody")
                                Log.e("Invalid response format", "${e.message}\nResponse: $responseBody")
                            }
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            onError("Error: ${response.message}\nResponse: $responseBody")
                            Log.e("Error", "${response.message}\nResponse: $responseBody")
                        }
                    }
                }
            }
        })
    }
}

data class CartResponse(val items: List<CartItem>, val morePages: Boolean)

private fun mainThreadCallback(callback: () -> Unit) {
    Handler(Looper.getMainLooper()).post(callback)
}