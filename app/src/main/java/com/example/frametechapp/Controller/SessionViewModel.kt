package com.example.frametechapp.Controller

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frame_tech_app.Data.CartItem
import com.example.frametech_app.Data.Category
import com.example.frametech_app.Data.Product
import com.example.frametechapp.Data.RepairRequest
import com.example.frametechapp.Data.SellListing
import com.example.frametechapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionViewModel(private val sessionManager: SessionManager, private val networkManager: NetworkManager,private val context: Context
) : ViewModel() {

    //tracking the refreshing the token
    private var retryCount = 0
    private val maxRetries = 3


    //State to track loading
    private val _isLoading = mutableStateOf(false)
    val isLoading = _isLoading
    //Cart tracking
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _cartMessage = MutableStateFlow<String?>(null)
    val cartMessage: StateFlow<String?> = _cartMessage

    private var currentPage = 1
    var morePagesAvailable = mutableStateOf(true)
    private val cartCache = mutableMapOf<Int, List<CartItem>>() // Cache for cart items
    // Products tracking
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>>  = _categories.asStateFlow()
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()
    //handling the session token
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Idle)
    val sessionState: StateFlow<SessionState> = _sessionState
     var currentToken: String? = null
    private var refreshJob: Job? = null
    //Errors tracking
    private val _error = MutableStateFlow<String?>(null)
    var error: StateFlow<String?> = _error.asStateFlow()

    private val _orderMessage = MutableStateFlow<String?>(null)
    val orderMessage: StateFlow<String?> = _orderMessage

    // Payment message tracking
    private val _paymentMessage = MutableStateFlow<String?>(null)
    val paymentMessage: StateFlow<String?> = _paymentMessage

    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethod>> = _paymentMethods

    init {
        fetchPaymentMethods()
    }

    private fun fetchPaymentMethods() {
        _paymentMethods.value = listOf(
            PaymentMethod("Credit Card", R.drawable.credit_card),
            PaymentMethod("Debit Card", R.drawable.debit_card),
            PaymentMethod("PayPal", R.drawable.ic_paypal),
            PaymentMethod("Google Pay", R.drawable.ic_google_pay),
            PaymentMethod("Bank Transfer", R.drawable.bank_transfer)
        )
    }

    fun login(email: String, password: String, onLoginSuccess: (String) -> Unit, onError: (String) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            networkManager.login(email, password, { token ->
                _isLoading.value = false
                sessionManager.saveToken(token)
                onLoginSuccess(
                    currentToken.toString()
                )
            }, { error ->
                _isLoading.value = false
                onError(error)
            })
        }
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
        _isLoading.value = true
        viewModelScope.launch {
            networkManager.register(
                firstName, lastName, email, phone, address, password,
                onSuccess = {
                    _isLoading.value = false
                    onSuccess()
                },
                onError = { error ->
                    _isLoading.value = false
                    onError(error)
                }
            )
        }
    }
    fun addToCart(productId: Int, quantity: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val token = sessionManager.getToken()
        if (token != null) {
            networkManager.addToCart(token, productId, quantity, {
                _cartMessage.value = it
                onSuccess()
            }, { error ->
                _cartMessage.value = error
                onError(error)
            })
        } else {
            onError("User is not logged in.")
        }
    }
    fun fetchCartItems(onError: (String) -> Unit) {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                val cachedItems = cartCache[currentPage]
                if (cachedItems != null) {
                    // Use cached data
                    _cartItems.value = cartItems.value + cachedItems
                    currentPage++
                } else {
                    // Fetch from network
                    networkManager.fetchCartItems(token, currentPage) { result ->
                        result.onSuccess { response ->
                            viewModelScope.launch {
                                // Update UI on the main thread
                                _cartItems.value = cartItems.value + response.items
                                cartCache[currentPage] = response.items // Cache the result
                                morePagesAvailable.value = response.morePages
                                currentPage++
                            }
                        }.onFailure { error ->
                            viewModelScope.launch {
                                // Show error on the main thread
                                withContext(Dispatchers.Main) {
                                    onError(error.message ?: "An unknown error occurred")
                                }
                            }
                        }
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    onError("User is not logged in.")
                }
            }
        }
    }

    fun updateCartItem(cartItemId: Int, quantity: Int, onError: (String) -> Unit) {
        val token = sessionManager.getToken()
        if (token != null) {
            networkManager.updateCartItem(token, cartItemId, quantity, {
                invalidateCache()
                fetchCartItems(onError) // Refresh the cart after update
            }, { error ->
                _cartMessage.value = error
                onError(error)
            })
        } else {
            onError("User is not logged in.")
        }
    }

    fun deleteCartItem(cartItemId: Int) {
        viewModelScope.launch {
            val token = sessionManager.getToken()
            if (token != null) {
                when (val result = networkManager.removeFromCart(token, cartItemId)) {
                    is NetworkResult.Success -> {
                        _cartItems.value = cartItems.value.filter { it.itemId != cartItemId }
                        _cartMessage.value = result.data
                        invalidateCache()
                    }
                    is NetworkResult.Error -> {
                        _cartMessage.value = result.message
                    }
                }
            } else {
                _cartMessage.value = "User is not logged in."
            }
        }
    }
    fun fetchCategories() {
        viewModelScope.launch {
            isLoading.value = true
            _error.value = null

            val result = networkManager.fetchCategories()
            result.onSuccess { fetchedCategories ->
                _categories.value = fetchedCategories
            }.onFailure { e ->
                _error.value = "Failed to fetch categories: ${e.message}"
            }

            isLoading.value = false
        }
    }

    fun fetchProducts(category: String, refresh: Boolean = false, page: Int = 1) {
        // If refresh is true, reset to initial state
        if (refresh) {
            _products.value = emptyList() // Clear current products
            morePagesAvailable.value = true // Reset page availability
        }

        // Check if more pages are available
        if (!morePagesAvailable.value) return

        val token = sessionManager.getToken()
        if (token.isNullOrEmpty()) {
            _error.value = "User not logged in"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true // Start loading state
            _error.value = null // Clear any existing errors

            // Fetch products using the given category and page number
            val result = networkManager.fetchProducts(token, category, page, PRODUCTS_PER_PAGE)
            result.onSuccess { (fetchedProducts, morePages) ->
                _products.value += fetchedProducts // Append new products to the existing list
                morePagesAvailable.value = morePages // Update availability of more pages

                // Update the current page if more products exist
                if (morePages) {
                    currentPage = page + 1
                }
            }.onFailure { e ->
                _error.value = "Failed to fetch products: ${e.message}"
            }

            _isLoading.value = false // End loading state
        }
    }

    fun createOrder(onSuccess: (String, Double) -> Unit, onError: (String) -> Unit) {
        val token = sessionManager.getToken()  // Retrieve the user's token from session
        if (token != null) {
            networkManager.createOrder(token, { orderId, totalPrice ->
                // Handle success: Update LiveData or any state as needed
                _orderMessage.value = "Order created successfully: $orderId"
                onSuccess(orderId, totalPrice)
            }, { error ->
                _orderMessage.value = error
                onError(error)
            })
        } else {
            onError("User is not logged in.")
        }
    }
    fun processPayment(orderId: String, paymentType: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val token = sessionManager.getToken()  // Retrieve the user's token from session
        if (token != null) {
            networkManager.processPayment(token, paymentType, orderId, {
                // Handle payment success
                _paymentMessage.value = "Payment processed successfully for Order: $orderId"
                onSuccess()
            }, { error ->
                _paymentMessage.value = error
                onError(error)
            })
        } else {
            onError("User is not logged in.")
        }
    }

    // Set the cart message
    fun setCartMessage(message: String) {
        _cartMessage.value = message
    }

    // Clear the cart message once displayed
    fun clearCartMessage() {
        _cartMessage.value = null
    }


    fun resetCartPagination() {
        currentPage = 1
        _cartItems.value = emptyList()
        morePagesAvailable.value = true
    }

    fun loadMoreCartItems(onError: (String) -> Unit) {
        if (morePagesAvailable.value) {
            fetchCartItems(onError)
        }
    }

    private fun invalidateCache() {
        cartCache.clear() // Clear the cache
    }
    fun logout(onLogout: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            sessionManager.clearSession()
            _isLoading.value = false
            onLogout()
        }
    }
    companion object {
        private const val PRODUCTS_PER_PAGE = 10
    }

    fun startTokenRefreshProcess(initialToken: String) {
        currentToken = initialToken
        refreshJob?.cancel() // Cancel any ongoing refresh jobs

        refreshJob = viewModelScope.launch {
            while (isActive) {
                delay(5 * 60 * 1000) // Wait for 5 minutes
                refreshToken() // Refresh the token
            }
        }
    }

    // Refresh token function with retry logic
    private fun refreshToken() {
        currentToken?.let { token ->
            _sessionState.value = SessionState.Refreshing

            networkManager.refreshToken(
                token = token,
                onSuccess = { newToken ->
                    retryCount = 0 // Reset retry count on success
                    currentToken = newToken
                    sessionManager.saveToken(newToken) // Save new token
                    _sessionState.value = SessionState.Active(newToken)
                },
                onError = { errorMessage ->
                    retryCount++
                    _sessionState.value = SessionState.Error(errorMessage)

                    if (retryCount <= maxRetries) {
                        viewModelScope.launch {
                            delay(30 * 1000) // Retry after 30 seconds
                            refreshToken() // Retry token refresh
                        }
                    } else {
                        logoutAndRedirect()  // Log user out and redirect after max retries
                    }
                }
            )
        }
    }

    // Logout and clear session
    fun logoutAndRedirect() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _sessionState.value = SessionState.Error("Session expired. Please log in again.")
            stopTokenRefreshProcess()
        }
    }

    // Verify session
    fun verifySession(onSuccess: (Boolean) -> Unit, onError: (String) -> Unit) {
        val token = sessionManager.getToken()
        if (token != null) {
            networkManager.verifySession(token, { isValid ->
                if (isValid) {
                    onSuccess(true)
                } else {
                    logoutAndRedirect()  // Logout and redirect if session is invalid
                    onError("Session invalid or expired.")
                }
            }, onError)
        } else {
            logoutAndRedirect()
            onError("User is not logged in.")
        }
    }
    fun stopTokenRefreshProcess() {
        refreshJob?.cancel() // Cancel the token refresh job
        refreshJob = null
    }


    override fun onCleared() {
        super.onCleared()
        refreshJob?.cancel()
    }


    // Request a repair
    fun requestRepair(description: String, bookedDate: String, imageUri: Uri,
         onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val token = sessionManager.getToken()
        if (token != null) {
            networkManager.requestRepair(
                context,
                token,
                description,
                bookedDate,
                imageUri,
                onSuccess,
                onError
            )
        } else {
            onError("User is not logged in.")
        }
    }

    // Sell items
    fun sellItems(description: String, price: String, imagePaths: List<Uri>, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val token = sessionManager.getToken()
        if (token != null) {
            networkManager.sellItems(context = context,token, description, price, imagePaths, onSuccess, onError)
        } else {
            onError("User is not logged in.")
        }
    }

    // View repair requests
    fun viewRepairRequests(onSuccess: (List<RepairRequest>) -> Unit, onError: (String) -> Unit) {
        val token = sessionManager.getToken()
        if (token != null) {
            networkManager.viewRepairRequests(token, onSuccess, onError)
        } else {
            onError("User is not logged in.")
        }
    }

    // View sell listings
    fun viewSellListings(onSuccess: (List<SellListing>) -> Unit, onError: (String) -> Unit) {
        val token = sessionManager.getToken()
        if (token != null) {
            networkManager.viewSellListings(token, onSuccess, onError)
        } else {
            onError("User is not logged in.")
        }
    }
}

sealed class SessionState {
    object Idle : SessionState()
    object Refreshing : SessionState()
    data class Active(val token: String) : SessionState()
    data class Error(val message: String) : SessionState()
}
