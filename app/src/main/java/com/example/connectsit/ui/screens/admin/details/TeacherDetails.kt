package com.example.connectsit.ui.screens.admin.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.connectsit.R
import com.example.connectsit.ui.screens.admin.details.rules.Validator

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDetails() {

    var TeacherName by remember { mutableStateOf("") }
    var TeacherUsername by remember { mutableStateOf("") }
    var TeacherPassword by remember { mutableStateOf("") }
    var TeacherEmailID by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    // State to track password visibility (Arif/Alex Please change the visibility Icon)
    var passwordVisible by remember { mutableStateOf(false) }
    //variable to check all validation status to forward it to FIrebase
    val isFormValid: Boolean = TeacherName.isNotEmpty() &&
            TeacherUsername.isNotEmpty() &&
            TeacherPassword.isNotEmpty() &&
            TeacherEmailID.isNotEmpty() &&
            nameError == null &&
            usernameError == null &&
            passwordError == null &&
            emailError == null

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
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = TeacherName,
            onValueChange = {
                TeacherName = it
                nameError = Validator.validateName(it) // Validate name
            },
            label = { Text("Name", color = Color.White) },
            isError = nameError != null,
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        if (nameError != null) {
            Text(text = nameError!!, color = Color.Red)
        }

        OutlinedTextField(
            value = TeacherUsername,
            onValueChange = {
                TeacherUsername = it
                usernameError = Validator.validateUsername(it) // Validate username
            },
            label = { Text("Username", color = Color.White) },
            isError = usernameError != null,
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        if (usernameError != null) {
            Text(text = usernameError!!, color = Color.Red)
        }

        OutlinedTextField(
            value = TeacherPassword,
            onValueChange = {
                TeacherPassword = it
                passwordError = Validator.validatePassword(it) // Validate password
            },
            label = { Text("Password", color = Color.White) },
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = android.R.drawable.ic_menu_view)
                else painterResource(id = android.R.drawable.ic_menu_close_clear_cancel)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    androidx.compose.foundation.Image(
                        painter = image,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            isError = passwordError != null
        )
        if (passwordError != null) {
            Text(text = passwordError!!, color = Color.Red)
        }

        OutlinedTextField(
            value = TeacherEmailID,
            onValueChange = {
                TeacherEmailID = it
                emailError = Validator.validateEmail(it) // Validate email
            },
            label = { Text("Email", color = Color.White) },
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            isError = emailError != null
        )
        if (emailError != null) {
            Text(text = emailError!!, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { /* Handle Save button click */ },
            modifier = Modifier.size(width = 170.dp, height = 50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White),
            enabled = isFormValid // Enable button only when the form is valid

        ) {
            Text(text = "SAVE")
        }
    }
}
