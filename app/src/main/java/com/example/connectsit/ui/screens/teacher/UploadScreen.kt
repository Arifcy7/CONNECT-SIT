package com.example.connectsit.ui.screens.teacher


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.connectsit.R
import com.example.connectsit.navigation.ScreenK

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(navController: NavController){
    val Bluish = Color(0xFF523EC8)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Bluish,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text(
                        "UPLOAD HERE",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ScreenK) }) {
                        Icon(
                            tint = Color.White,
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "BACK"
                        )
                    }
                }
            )

        },

        ) { innerPadding ->
        Surface(modifier = Modifier
            .padding(innerPadding)
            .background(color = Color.Black)
            .size(500.dp)
                ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                Image(painter = painterResource(id = R.drawable.img_2),
                    contentDescription = "Upload Image",
                    modifier = Modifier.clickable {/* Handle image click */})
                Text(
                    text = "CLICK TO UPLOAD",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(16.dp)
                )
                    Button(onClick = { /*TODO*/ },modifier = Modifier
                        .size(width = 180.dp, height = 120.dp),
                        colors = androidx.compose.material3.ButtonDefaults
                            .buttonColors(containerColor = Bluish)) {
                        Text(text = "UPLOAD")
                    }

                    //PROGRESS BAR OF THINGS THAT HAD BEEN DONE UPLOADING
            }



        }
    }


}


@Preview(showBackground = true)
@Composable
fun UploadScreenPreview() {
    UploadScreen(navController = rememberNavController())
}