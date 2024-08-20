@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.connectsit.ui.theme

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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.serialization.generateRouteWithArgs
import com.example.connectsit.R
import com.example.connectsit.ScreenA
import com.example.connectsit.ScreenB
import com.example.connectsit.ScreenC
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun LoginScreen(enterer: String?) {
    val navController = rememberNavController()

    val textColor1 = Color(color = 0xFFFFFFFF)
    var email by remember { mutableStateOf(value = "") }
    var password by remember { mutableStateOf(value = "") }
    val emailFocusRequester = FocusRequester()
    val passwordFocusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
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
                color = textColor1,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "LOGIN AS $enterer",color = textColor1)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,

                onValueChange = {email = it},
                label = {
                    Text(text = "Email Address", color = textColor1)
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
                        textColor1
                    }, textStyle = TextStyle(color = textColor1),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor1, // Input text color
                    focusedBorderColor = Color.Blue, // Border color when focused
                    unfocusedBorderColor = Color.White // Border color when not focused
                )

            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = {
                    Text(text = "PASSWORD",color = textColor1)},

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
                    textColor1, // Input text color
                    focusedBorderColor = Color.Blue, // Border color when focused
                    unfocusedBorderColor = Color.White // Border color when not focused
                )
                )
            Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {if (enterer=="TEACHER"&& email=="apsit"&& password=="123"){
                    //Handle login
            }
            else if (enterer=="STUDENT"&& email=="apsit"&& password=="123"){
                // Handle login action here
            }  },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue, // Background color
                contentColor = Color.White   // Text color
            ),
            modifier = Modifier
        ) {
            Text(text = "Login",color = textColor1)
        }
            Spacer(modifier = Modifier.height(16.dp))
            if (enterer=="STUDENT"){
            Text( text = "IF FORGET PASSWORD CONTACT YOUR TEACHERS",color = textColor1)}
            else{
                Text(modifier = Modifier
                    .clickable { }, text = "FORGET PASSWORD?",color = textColor1)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
}


