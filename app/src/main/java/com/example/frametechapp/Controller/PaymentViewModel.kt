package com.example.frametechapp.Controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frametechapp.R
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Initial)
    val uiState: StateFlow<PaymentUiState> = _uiState

    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethod>> = _paymentMethods

    init {
        fetchPaymentMethods()
    }

    fun initiatePayment(amount: Double, currency: String) {
        viewModelScope.launch {
            _uiState.value = PaymentUiState.Loading
            try {
                val paymentIntentClientSecret = createPaymentIntent(amount, currency)
                _uiState.value = PaymentUiState.PaymentReady(paymentIntentClientSecret)
            } catch (e: Exception) {
                _uiState.value = PaymentUiState.Error("Failed to initiate payment: ${e.message}")
            }
        }
    }

    private suspend fun createPaymentIntent(amount: Double, currency: String): String {
        // This should be a call to your backend to create a PaymentIntent
        // For demo purposes, we're returning a dummy client secret
        return "pi_1234567890_secret_1234567890"
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

    fun onPaymentResult(result: PaymentSheetResult) {
        when (result) {
            is PaymentSheetResult.Completed -> {
                _uiState.value = PaymentUiState.PaymentSuccess
            }
            is PaymentSheetResult.Canceled -> {
                _uiState.value = PaymentUiState.Initial
            }
            is PaymentSheetResult.Failed -> {
                _uiState.value = PaymentUiState.Error("Payment failed: ${result.error}")
            }
        }
    }
}
sealed class PaymentUiState {
    object Initial : PaymentUiState()
    object Loading : PaymentUiState()
    data class Error(val message: String) : PaymentUiState()
    data class PaymentReady(val clientSecret: String) : PaymentUiState()
    object PaymentSuccess : PaymentUiState()
}
data class PaymentMethod(val name: String, val iconRes: Int)