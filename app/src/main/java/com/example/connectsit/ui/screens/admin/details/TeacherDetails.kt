package com.example.connectsit.ui.screens.admin.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.ui.Modifier

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import com.example.connectsit.R.drawable.*
import androidx.compose.foundation.Image as Image
import androidx.compose.ui.res.painterResource as painterResource1


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDetails() {

    var TeacherName by remember { mutableStateOf("") }
    var TeacherUsername by remember { mutableStateOf("") }
    var TeacherPassword by remember { mutableStateOf("") }
    var TeacherMobileNumber by remember { mutableStateOf("") }


    Column(modifier = androidx.compose.ui.Modifier
        .fillMaxSize()
        .background(color = Color.Black)
        ,verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource1(id = img),
            contentDescription = "Login image",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))
        Text(text = "ADD TEACHER", fontSize = 30.sp , fontWeight = FontWeight.Bold,color = Color.White)
        Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))
        Text(text = "Student Details Form", fontSize = 24.sp)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            keyboardOptions = KeyboardOptions.run {
                Default.copy(
                        imeAction = ImeAction.Next
                    )
            },
            textStyle = TextStyle(color = Color.White),
            value = TeacherName,
            onValueChange = { TeacherName = it },
            label = { Text("Name" , color = Color.White) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                Color.White, // Input text color
                focusedBorderColor = Color.Blue, // Border color when focused
                unfocusedBorderColor = Color.White // Border color when not focused
            )

        )


        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            textStyle = TextStyle(color = Color.White),

            value = TeacherUsername,
            onValueChange = { TeacherUsername = it },
            label = { Text("Username", color = Color.White) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                Color.White, // Input text color
                focusedBorderColor = Color.Blue, // Border color when focused
                unfocusedBorderColor = Color.White // Border color when not focused
            )
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            textStyle = TextStyle(color = Color.White),
            value = TeacherPassword,
            onValueChange = { TeacherPassword = it },
            label = { Text("Password", color = Color.White) },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                Color.White, // Input text color
                focusedBorderColor = Color.Blue, // Border color when focused
                unfocusedBorderColor = Color.White // Border color when not focused
            )
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            textStyle = TextStyle(color = Color.White),
            value = TeacherMobileNumber,
            onValueChange = { TeacherMobileNumber = it },
            label = { Text("Mobile Number", color = Color.White)},
            colors = TextFieldDefaults.outlinedTextFieldColors(
                Color.White, // Input text color
                focusedBorderColor = Color.Blue, // Border color when focused
                unfocusedBorderColor = Color.White // Border color when not focused
            )

        )
        Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))
        Button( modifier = androidx.compose.ui.Modifier.size(width = 170.dp, height = 50.dp),
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text(text = "SAVE")
        }
       //COURSE ALLOTING REMAINING
    }



}


