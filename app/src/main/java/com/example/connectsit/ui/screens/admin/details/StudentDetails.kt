package com.example.connectsit.ui.screens.admin.details

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.connectsit.R
import com.example.connectsit.navigation.ScreenE
import com.example.connectsit.ui.screens.admin.details.rules.Validator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetails(NavController : NavController) {
    val context  = LocalContext.current
    var studentName by remember { mutableStateOf("") }
    var studentUsername by remember { mutableStateOf("") }
    var studentPassword by remember { mutableStateOf("") }
    var studentEmailID by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    var passwordVisible by remember { mutableStateOf(false) }

    val isFormValid: Boolean = studentName.isNotEmpty() &&
            studentUsername.isNotEmpty() &&
            studentPassword.isNotEmpty() &&
            studentEmailID.isNotEmpty() &&
            nameError == null &&
            usernameError == null &&
            passwordError == null &&
            emailError == null

    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

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

        OutlinedTextField(
            value = studentName,
            onValueChange = {
                studentName = it
                nameError = Validator.validateName(it)
            },
            label = { Text("Name", color = Color.White) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
            ),
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
            value = studentUsername,
            onValueChange = {
                studentUsername = it
                usernameError = Validator.validateUsername(it)
            },
            label = { Text("Username", color = Color.White) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
            ),
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
            value = studentPassword,
            onValueChange = {
                studentPassword = it
                passwordError = Validator.validatePassword(it)
            },
            label = { Text("Password", color = Color.White) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
            ),
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
            value = studentEmailID,
            onValueChange = {
                studentEmailID = it
                emailError = Validator.validateEmail(it)
            },
            label = { Text("Email", color = Color.White) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
            ),
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
            onClick = {
                if (isFormValid) {
                    // Register user with Firebase Authentication
                    auth.createUserWithEmailAndPassword(studentEmailID, studentPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val userData = mapOf(
                                    "name" to studentName,
                                    "username" to studentUsername,
                                    "email" to studentEmailID,
                                    "password" to studentPassword
                                )

                                firestore.collection("students")
                                    .document(user?.uid ?: "")
                                    .set(userData)
                                    .addOnSuccessListener {
                                        println("Data uploaded successfully")
                                        Log.d("FireStore", "StudentDetails Uploaded Successfully")
                                        Toast.makeText(context,
                                            "STUDENT ADDED SUCCESSFULLY!!",
                                            Toast.LENGTH_LONG).show()

                                    }
                                    .addOnFailureListener {
                                        println("Error uploading data: ${it.message}")
                                        Log.w("FireStore", "Error Adding Details", it)
                                    }


                                NavController.navigate(ScreenE)
                            } else {
                                println("Error registering user: ${task.exception?.message}")
                                Log.w("FirebaseAuth", "Error Registering User", task.exception)
                            }
                        }
                }
            },
            modifier = Modifier.size(width = 170.dp, height = 50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White),
            enabled = isFormValid
        ) {
            Text(text = "SAVE")
        }
    }
}
