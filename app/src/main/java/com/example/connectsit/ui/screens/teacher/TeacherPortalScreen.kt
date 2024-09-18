package com.example.connectsit.ui.screens.teacher



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.connectsit.R
import com.example.connectsit.navigation.ScreenK


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherPortalScreen(navController: NavController) {
    val Bluish = Color(0xFF523EC8)
        val i = 0
        var ModuleCounter = 1
        val Modules = listOf("Module 1", "Module 2", "Module 3", "Module 4", "Module 5")
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Bluish,
                        titleContentColor = Color.White,
                    ),
                    title = {
                        Text(
                            "YOUR COURSES",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                tint = Color.White,
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"

                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,

                    )
            },

            ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(color = Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(20.dp))
                UploadChoices("notices", navController)
                UploadChoices(" notes", navController)
                UploadChoices(" other", navController)
            }
        }
}

fun yourCourses(){

}
@Composable
fun UploadChoices(category: String,navController: NavController){
    val Bluish = Color(0xFF523EC8)
    val Buttoncolo = Color(0xFF2A1C9F)
    Row(modifier = Modifier
            .size(width = 400.dp, height = 220.dp)
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .background(color = Bluish, shape = RoundedCornerShape(20.dp))) {
        when (category) {
            "notices" -> Image(
                painter = painterResource(id = R.drawable.notices), contentDescription = "info",
                modifier = Modifier
                    .padding(horizontal = 17.dp, vertical = 25.dp)
                    .clip(RoundedCornerShape(20))
            )

            " notes" -> Image(
                painter = painterResource(id = R.drawable.notes), contentDescription = "info",
                modifier = Modifier
                    .padding(horizontal = 17.dp, vertical = 25.dp)
                    .clip(RoundedCornerShape(20))
            )

            " other" -> Image(
                painter = painterResource(id = R.drawable.other), contentDescription = "info",
                modifier = Modifier
                    .padding(horizontal = 17.dp, vertical = 25.dp)
                    .clip(RoundedCornerShape(20))
            )


        }
        Column() {
            Text(
                text = category.toUpperCase(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier.padding(horizontal = 17.dp, vertical = 30.dp)
            )
            Button(
                onClick = { navController.navigate(ScreenK) }, modifier = Modifier
                    .padding(horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Buttoncolo)
            ) {
                Text(text = "UPLOAD")
            }

        }
}
}






