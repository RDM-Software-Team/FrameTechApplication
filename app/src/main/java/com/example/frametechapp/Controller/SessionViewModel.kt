package com.example.frametechapp.Controller

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frame_tech_app.Data.CartItem
import com.example.frametech_app.Data.Category
import com.example.frametech_app.Data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionViewModel(private val sessionManager: SessionManager, private val networkManager: NetworkManager) : ViewModel() {
    //State to track loading
    private val _isLoading = mutableStateOf(false)
    val isLoading = _isLoading
    //Cart tracking
    val cartMessage = mutableStateOf<String?>(null)
    val cartItems = mutableStateOf<List<CartItem>>(emptyList())
    private var currentPage = 1
    var morePagesAvailable = mutableStateOf(true)
    private val cartCache = mutableMapOf<Int, List<CartItem>>() // Cache for cart items
    // Products tracking
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>>  = _categories.asStateFlow()
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    //Errors tracking
    private val _error = MutableStateFlow<String?>(null)
    var error: StateFlow<String?> = _error.asStateFlow()


    fun login(email: String, password: String, onLoginSuccess: () -> Unit, onError: (String) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            networkManager.login(email, password, { token ->
                _isLoading.value = false
                sessionManager.saveToken(token)
                onLoginSuccess()
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
                cartMessage.value = it
                onSuccess()
            }, { error ->
                cartMessage.value = error
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
                    cartItems.value = cartItems.value + cachedItems
                    currentPage++
                } else {
                    // Fetch from network
                    networkManager.fetchCartItems(token, currentPage,
                        { items, morePages ->
                            viewModelScope.launch {
                                // Update UI on the main thread
                                cartItems.value = cartItems.value + items
                                cartCache[currentPage] = items // Cache the result
                                morePagesAvailable.value = morePages
                                currentPage++
                            }
                        },
                        { error ->
                            viewModelScope.launch {
                                // Show error on the main thread
                                withContext(Dispatchers.Main) {
                                    onError(error)
                                }
                            }
                        }
                    )
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
                cartMessage.value = error
                onError(error)
            })
        } else {
            onError("User is not logged in.")
        }
    }

    fun deleteCartItem(cartItemId: Int, onError: (String) -> Unit) {
        val token = sessionManager.getToken()
        if (token != null) {
            networkManager.removeFromCart(token, cartItemId, {
                cartItems.value = cartItems.value.filter { it.itemId != cartItemId }
                cartMessage.value = "Item deleted"
                invalidateCache()
            }, { error ->
                cartMessage.value = error
                onError(error)
            })
        } else {
            onError("User is not logged in.")
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
                _products.value = _products.value + fetchedProducts // Append new products to the existing list
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


    fun resetCartPagination() {
        currentPage = 1
        cartItems.value = emptyList()
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
}