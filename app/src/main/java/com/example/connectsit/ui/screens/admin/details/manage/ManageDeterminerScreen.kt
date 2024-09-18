package com.example.connectsit.ui.screens.admin.details.manage

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.connectsit.R
import com.example.connectsit.navigation.ScreenI
import com.example.connectsit.navigation.ScreenJ

@Composable
fun ManageDeterminerScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) { Image(painter = painterResource(id = R.drawable.img), contentDescription ="LOGO" ,
        modifier = Modifier.size(200.dp))



        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.navigate(ScreenI) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            ), modifier = Modifier.size(width = 165.dp, height = 40.dp)
            ) {
            Text(text = "Manage Students")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { navController.navigate(ScreenJ)},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            ),
            modifier = Modifier.size(width = 165.dp, height = 40.dp)) {
            Text(text = "Manage Teachers")
            Modifier.background(color = Color.Blue)
        }
        val context = LocalContext.current
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                // Launch the AdminActivity using Intent
                Log.d("CoursesError", "ManageDeterminerScreen: This Button Works")
                val intent = Intent(context, AdminActivity::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            ),
            modifier = Modifier.size(width = 165.dp, height = 40.dp)
        ) {
            Text(text = "Manage Courses")
            Modifier.background(color = Color.Blue)
        }

    }





}

