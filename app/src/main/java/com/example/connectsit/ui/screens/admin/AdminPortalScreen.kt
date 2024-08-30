package com.example.connectsit.ui.screens.admin


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
import androidx.navigation.NavController
import com.example.connectsit.R
import com.example.connectsit.navigation.ScreenF


@Composable
fun AdminPortalScreen(NavController: NavController)  {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Login image",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button( modifier = Modifier.size(width = 150.dp, height = 40.dp),
            onClick = {
                NavController.navigate(ScreenF)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text(text = "ADD STUDENT")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button( modifier = Modifier.size(width = 150.dp, height = 40.dp),
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text( text = "ADD TEACHER")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button( modifier = Modifier.size(width = 150.dp, height = 40.dp),
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text( text = "MANAGE ALL")
        }
    }

}


