package com.example.connectsit.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.connectsit.R
import com.example.connectsit.ui.model.Enterers

// moved this composable to its own file to remove clutter in MainActivity
@Composable
fun StudentTeacherDeterminerScreen(
    // create anonymous function to pass back navigation to MainActivity
    handleNavigation: (Enterers) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )

    {
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Login image",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Created Enum to safely determine which user/enterer we are displaying for
                handleNavigation(Enterers.STUDENT)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text(text = "STUDENT")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Created Enum to safely determine which user/enterer we are displaying for
                handleNavigation(Enterers.TEACHER)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text(text = "TEACHERS")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}