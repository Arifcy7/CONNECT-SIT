package com.example.connectsit.ui.screens.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun AboutPage(navController: NavController) {
    var showDialog by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize().background(color = Color.Black), contentAlignment = Alignment.Center) {
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("About Us") },
                text = {
                    Text(
                        "ConnectSIT is an innovative app designed to enhance communication " +
                                "between teachers and students. Our mission is to create a seamless " +
                                "learning experience by providing a platform for easy information sharing " +
                                "and collaboration."
                    )
                },
                confirmButton = {
                    Button(onClick = { showDialog = false; navController.navigateUp() },colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White)) {
                        Text("Close")
                    }
                }
            )
        }
    }
}