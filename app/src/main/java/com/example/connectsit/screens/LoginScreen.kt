@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.connectsit.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.connectsit.R
import com.example.connectsit.model.Enterers
import com.example.connectsit.ui.theme.White

typealias Username = String
typealias Password = String


@Composable
fun LoginScreen(
    // Created Enum to safely determine which user/enterer we are displaying for
    enterer: Enterers,
    // create anonymous function to pass back navigation to MainActivity
    handleLogin: ( Username, Password) -> Unit
) {
    val email = remember { mutableStateOf(value = "") }
    val password = remember { mutableStateOf(value = "") }
    val emailFocusRequester = FocusRequester()
    val passwordFocusRequester = FocusRequester()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Login image",
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = "CONNECT'SIT",
            color = White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "LOGIN AS $enterer", color = White)
        Spacer(modifier = Modifier.height(16.dp))
        //Created composable to reduce clutter
        EmailTextField(
            email = email,
            emailFocusRequester = emailFocusRequester,
            passwordFocusRequester = passwordFocusRequester
        )
        Spacer(modifier = Modifier.height(16.dp))
        //Created composable to reduce clutter
        PasswordTextField(
            password = password,
            passwordFocusRequester = passwordFocusRequester
        )
        Spacer(modifier = Modifier.height(16.dp))
        //Created composable to reduce clutter
        LoginButton(
            onClick = {
                //Consider error handling via UI on callback by passing callback: () -> Unit in handle login fn
                handleLogin(email.value, password.value)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ForgotPasswordText(enterer = enterer)
    }
}

@Composable
fun EmailTextField(
    email: MutableState<String>,
    emailFocusRequester: FocusRequester,
    passwordFocusRequester: FocusRequester
) {
    OutlinedTextField(
        value = email.value,

        onValueChange = { email.value = it },
        label = {
            Text(text = "Email Address", color = White)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(
            onNext = { passwordFocusRequester.requestFocus() }
        ),
        modifier = Modifier
            .focusRequester(emailFocusRequester)

            .onFocusChanged { focusState ->
                White
            }, textStyle = TextStyle(color = White),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            White, // Input text color
            focusedBorderColor = Color.Blue, // Border color when focused
            unfocusedBorderColor = Color.White // Border color when not focused
        )
    )
}

@Composable
fun PasswordTextField(
    password: MutableState<String>,
    passwordFocusRequester: FocusRequester
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = password.value,
        onValueChange = { password.value = it },
        label = {
            Text(text = "PASSWORD", color = White)
        },

        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        ),
        modifier = Modifier.focusRequester(passwordFocusRequester),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            White, // Input text color
            focusedBorderColor = Color.Blue, // Border color when focused
            unfocusedBorderColor = Color.White // Border color when not focused
        )
    )
}

@Composable
fun LoginButton(
    onClick: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue, // Background color
            contentColor = Color.White   // Text color
        ),
        modifier = Modifier,
        onClick = onClick
    ) {
        Text(text = "Login", color = White)
    }
}

@Composable
fun ForgotPasswordText(enterer: Enterers) {
    when (enterer) {
        Enterers.STUDENT -> {
            Text(text = "IF FORGET PASSWORD CONTACT YOUR TEACHERS", color = White)
        }
        Enterers.TEACHER -> {
            Text(
                modifier = Modifier
                    .clickable { }, text = "FORGET PASSWORD?", color = White
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}