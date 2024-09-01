package com.example.connectsit.ui.screens.admin.details

import android.widget.Toast
import android.util.Log
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
fun TeacherDetails(NavController : NavController) {
    val context  = LocalContext.current
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
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = TeacherName,
            onValueChange = {
                TeacherName = it
                nameError = Validator.validateName(it) // Validate name
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
            value = TeacherUsername,
            onValueChange = {
                TeacherUsername = it
                usernameError = Validator.validateUsername(it) // Validate username
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
            value = TeacherPassword,
            onValueChange = {
                TeacherPassword = it
                passwordError = Validator.validatePassword(it) // Validate password
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
            value = TeacherEmailID,
            onValueChange = {
                TeacherEmailID = it
                emailError = Validator.validateEmail(it) // Validate email
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
                Toast.makeText(context,
                    "TEACHER ADDED SUCCESSFULLY!!",
                    Toast.LENGTH_LONG).show()
                NavController.navigate(ScreenE)

                if (isFormValid) {
                    // Register user with Firebase Authentication
                    auth.createUserWithEmailAndPassword(TeacherEmailID, TeacherPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val userData = mapOf(
                                    "name" to TeacherName,
                                    "username" to TeacherUsername,
                                    "email" to TeacherEmailID,
                                    "password" to TeacherPassword
                                )

                                firestore.collection("Teachers")
                                    .document(user?.uid ?: "")
                                    .set(userData)
                                    .addOnSuccessListener {
                                        println("Data uploaded successfully")
                                        Log.d("FireStore" ,"TeacherDetails: Uploaded Successfully ")
                                    }
                                    .addOnFailureListener {
                                        println("Error uploading data: ${it.message}")
                                        Log.w("FireStore", "Error Adding Details", it )
                                    }
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
