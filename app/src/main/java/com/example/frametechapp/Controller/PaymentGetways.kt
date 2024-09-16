package com.example.frametechapp.Controller

//import android.app.Activity
//import android.content.Intent
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
//import com.google.android.gms.wallet.AutoResolveHelper
//import com.google.android.gms.wallet.PaymentData
//import com.google.android.gms.wallet.PaymentDataRequest
//import com.google.android.gms.wallet.TransactionInfo
//import com.google.android.gms.wallet.Wallet
//import com.google.android.gms.wallet.WalletConstants
//import com.paypal.android.sdk.payments.PayPalPayment
//import com.paypal.android.sdk.payments.PayPalService
//import com.paypal.android.sdk.payments.PaymentActivity
//import com.paypal.android.sdk.payments.PaymentConfirmation
//import com.stripe.android.Stripe
//import com.stripe.android.paymentsheet.PaymentSheet
//import com.stripe.android.paymentsheet.PaymentSheetContract
//import com.stripe.android.paymentsheet.PaymentSheetResult
//import java.math.BigDecimal
//
//class PaymentGetways {
//
//    @Composable
//    fun CreditDebitCardPayment(stripe: Stripe) {
//        val context = LocalContext.current
//        val stripePaymentLauncher = rememberLauncherForActivityResult(
//            contract = PaymentSheetContract(),
//            onResult = { result ->
//                when (result) {
//                    is PaymentSheetResult.Completed -> {
//                        Toast.makeText(context, "Payment Successful!", Toast.LENGTH_SHORT).show()
//                    }
//                    is PaymentSheetResult.Failed -> {
//                        Toast.makeText(context, "Payment Failed: ${result.error.message}", Toast.LENGTH_SHORT).show()
//                    }
//                    is PaymentSheetResult.Canceled -> {
//                        Toast.makeText(context, "Payment Canceled", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        )
//
//        Button(onClick = {
//            // Use your backend to create PaymentIntent and get clientSecret
//            val clientSecret = "YOUR_PAYMENT_INTENT_CLIENT_SECRET"
//            val configuration = PaymentSheet.Configuration(
//                merchantDisplayName = "Your Business",
//                allowsDelayedPaymentMethods = true
//            )
//            stripePaymentLauncher.launch(PaymentSheet.PresentationParams(clientSecret, configuration))
//        }) {
//            Text(text = "Pay with Credit/Debit Card")
//        }
//    }
//
//    @Composable
//    fun PayPalPayments() {
//        val context = LocalContext.current
//        val payPalService = PayPalService()
//
//        Button(onClick = {
//            val payment = PayPalPayment(BigDecimal("10.00"), "USD", "Test Payment", PayPalPayment.PAYMENT_INTENT_SALE)
//            val intent = Intent(context, PaymentActivity::class.java)
//            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalService)
//            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
//            (context as Activity).startActivityForResult(intent, 123)
//        }) {
//            Text(text = "Pay with PayPal")
//        }
//    }
//    @Composable
//    fun HandlePayPalResult(data: Intent?) {
//        val result = data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
//        result?.let {
//            if (result.proofOfPayment.state == "approved") {
//                Toast.makeText(LocalContext.current, "Payment successful!", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(LocalContext.current, "Payment failed!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    @Composable
//    fun GooglePayPayment() {
//        val context = LocalContext.current
//
//        // Google Pay request
//        val paymentDataRequest = PaymentDataRequest.Builder()
//            .setTransactionInfo(
//                TransactionInfo.newBuilder()
//                    .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
//                    .setTotalPrice("10.00")
//                    .setCurrencyCode("USD")
//                    .build()
//            )
//            .setAllowedPaymentMethods(listOf(WalletConstants.PAYMENT_METHOD_CARD))
//            .build()
//
//        val paymentsClient = Wallet.getPaymentsClient(
//            context,
//            Wallet.WalletOptions.Builder()
//                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
//                .build()
//        )
//
//        val googlePayLauncher = rememberLauncherForActivityResult(
//            contract = AutoResolveHelper.PaymentDataContract(),
//            onResult = { resultCode, data ->
//                if (resultCode == Activity.RESULT_OK) {
//                    val paymentData = PaymentData.getFromIntent(data)
//                    val token = paymentData?.paymentMethodToken?.token
//                    if (token != null) {
//                        Toast.makeText(context, "Payment Successful", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(context, "Payment Failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//        )
//
//        Button(onClick = {
//            googlePayLauncher.launch(paymentDataRequest)
//        }) {
//            Text(text = "Pay with Google Pay")
//        }
//    }
//
//
//    @Composable
//    fun BankTransferPayment() {
//        val activity = LocalContext.current as Activity
//        val checkout = Checkout()
//        checkout.setKeyID("YOUR_RAZORPAY_KEY_ID")
//
//        val options = JSONObject().apply {
//            put("name", "Your Business Name")
//            put("description", "Bank Transfer Payment")
//            put("currency", "INR")
//            put("amount", "50000")  // Amount in paisa
//            put("method", "netbanking")  // Specific to Bank Transfer
//        }
//
//        Button(onClick = {
//            checkout.open(activity, options)
//        }) {
//            Text(text = "Pay via Bank Transfer")
//        }
//    }
//}
