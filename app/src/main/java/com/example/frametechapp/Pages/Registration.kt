package com.example.frame_tech_app.Pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frametechapp.Controller.SessionViewModel
import com.example.frametechapp.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, DelicateCoroutinesApi::class)
@Composable
fun Registration(
    sessionViewModel: SessionViewModel,
    onRegisterSuccess: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isLoading by sessionViewModel.isLoading

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Computer Complex",
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        InputField(
            value = firstName,
            onValueChange = { firstName = it; errorMessage = null },
            label = "First Name",
            icon = R.drawable.default_person,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        InputField(
            value = lastName,
            onValueChange = { lastName = it; errorMessage = null },
            label = "Last Name",
            icon = R.drawable.default_person,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        InputField(
            value = email,
            onValueChange = { email = it; errorMessage = null },
            label = "Email",
            icon = R.drawable.default_email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        InputField(
            value = address,
            onValueChange = { address = it; errorMessage = null },
            label = "Address",
            icon = R.drawable.default_email,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        InputField(
            value = phone,
            onValueChange = { phone = it; errorMessage = null },
            label = "Phone Number",
            icon = R.drawable.default_numbers,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        InputField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            label = "Password",
            icon = R.drawable.default_password,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            isPassword = true
        )

        InputField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it; errorMessage = null },
            label = "Confirm Password",
            icon = R.drawable.default_password,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            isPassword = true,
            isError = password != confirmPassword && confirmPassword.isNotEmpty()
        )

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedContent(
            targetState = isLoading,
            transitionSpec = { fadeIn() with fadeOut() }
        ) { loading ->
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = {
                        if (validateInputs(firstName, lastName, email, address, phone, password, confirmPassword)) {
                            sessionViewModel.register(
                                firstName, lastName, email, phone, address, password,
                                onSuccess = {
                                    // This should navigate back to the login page
                                    onRegisterSuccess()
                                },
                                onError = { error ->
                                    errorMessage = error
                                }
                            )
                        } else {
                            errorMessage = "Please fill all fields correctly"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }
            }
        }

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    isPassword: Boolean = false,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        isError = isError,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

fun validateInputs(
    firstName: String, lastName: String, email: String,
    address: String, phone: String, password: String, confirmPassword: String
): Boolean {
    return firstName.isNotBlank() && lastName.isNotBlank() &&
            email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            address.isNotBlank() && phone.isNotBlank() &&
            password.isNotBlank() && password == confirmPassword
}
@Composable
fun VerifyPassword(password:String){
    val count = 9
    val specialChar = arrayOf('!','@','#','$','%','^','&','*','(',')','`','~')
    val upperCase = arrayOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z')
    val lowerCase = arrayOf('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z')
    val digits = arrayOf('0','1','2','3','4','5','6','7','8','9')
    val conditions = listOf(
        "Password must be 9 characters long",
        "Password must contain an uppercase letter",
        "Password must contain a lowercase letter",
        "Password must contain a special character",
        "Password must contain a number"
    )

    var isSpecialChar = false
    var isUppercase = false
    var isDigits = false
    var isLower = false

    if(password.isNotEmpty()){
        for (upper in upperCase){
            if (password.contains(upper)) {
                isUppercase = true
                break
            }
        }
        for (lower in lowerCase){
            if (password.contains(lower)) {
                isLower = true
                break
            }
        }
        for (special in specialChar){
            if (password.contains(special)) {
                isSpecialChar = true
                break
            }
        }
        for (nums in digits){
            if (password.contains(nums)) {
                isDigits = true
                break
            }
        }
        // Checking conditions
        val isValidPassword = password.length >= count && isUppercase && isLower && isSpecialChar && isDigits

        // Display UI
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            conditions.forEachIndexed { index, condition ->
                Text(
                    text = condition,
                    color = if (index == 0 && password.length < count || (index == 1 && !isUppercase) ||
                        (index == 2 && !isLower) || (index == 3 && !isSpecialChar) || (index == 4 && !isDigits))
                        Color.Red
                    else
                        Color.Green
                )
            }
            Text(
                text = "Password is ${if (isValidPassword) "valid" else "invalid"}",
                color = if (isValidPassword) Color.Green else Color.Red
            )
        }
    }
}
