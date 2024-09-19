@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.connectsit.ui.screens.auth.login

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexafit.fitjournal.core.presentation.commoncomponents.dialogs.LoadingScreenDialog
import com.example.connectsit.R
import com.example.connectsit.core.theme.White
import com.example.connectsit.ui.model.Enterers
import com.example.connectsit.ui.model.login.LoginModel
import com.example.connectsit.ui.model.login.LoginState
import kotlinx.coroutines.delay



@Composable
fun LoginScreen(
    loginState: LoginState,
    handleLogin: (LoginModel) -> Unit
) {
    val username = remember { mutableStateOf(value = "") }
    val password = remember { mutableStateOf(value = "") }
    val usernameFocusRequester = FocusRequester()
    val passwordFocusRequester = FocusRequester()
    var loginFailed by remember { mutableStateOf(false) }

    // Shake animation
    val shakeAnimation = rememberInfiniteTransition()
    val shakeOffset by shakeAnimation.animateFloat(
        initialValue = 0f,
        targetValue = if (loginFailed) 10f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(50, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Shake screen"
    )

    LaunchedEffect(loginFailed) {
        if (loginFailed) {
            delay(300) // 3 seconds
            loginFailed = false
        }
    }

    if (loginState.showLoadingScreen) {
        LoadingScreenDialog(
            onBackPress = {}
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .offset {
                IntOffset(
                    (if (loginFailed) shakeOffset.dp else 0.dp)
                        .toPx()
                        .toInt(), 0
                )
            }, // Apply the shake offset
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
        Text(text = "LOGIN AS ${loginState.userType.name}", color = White)
        Spacer(modifier = Modifier.height(16.dp))

        UsernameTextField(
            username = username,
            usernameFocusRequester = usernameFocusRequester,
            passwordFocusRequester = passwordFocusRequester
        )
        Spacer(modifier = Modifier.height(16.dp))

        PasswordTextField(
            password = password,
            passwordFocusRequester = passwordFocusRequester
        )
        Spacer(modifier = Modifier.height(16.dp))

        LoginButton(
            onClick = {
                handleLogin(
                    LoginModel(
                        username = username.value,
                        password = password.value,
                        userType = loginState.userType,
                        onLoginErrorCallback = { showErrorAnimation ->
                            loginFailed = showErrorAnimation
                        }
                    )
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        ForgotPasswordText(enterer = loginState.userType)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameTextField(
    username: MutableState<String>,
    usernameFocusRequester: FocusRequester,
    passwordFocusRequester: FocusRequester
) {
    OutlinedTextField(
        value = username.value,
        onValueChange = { username.value = it },
        label = {
            Text(text = "Username", color = White)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onNext = { passwordFocusRequester.requestFocus() }
        ),
        modifier = Modifier
            .focusRequester(usernameFocusRequester)
            .onFocusChanged { focusState ->
                // No operation needed
            },
        textStyle = TextStyle(color = White),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            White, // Input text color
            focusedBorderColor = Color.Blue, // Border color when focused
            unfocusedBorderColor = Color.White // Border color when not focused
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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

        Enterers.ADMIN -> {
            Text(
                modifier = Modifier.clickable { },
                text = "FORGET PASSWORD?", color = White
            )
        }
    }
}