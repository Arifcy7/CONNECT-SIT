package com.example.connectsit.ui.screens.student

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.connectsit.R
import com.example.connectsit.navigation.ScreenA
import com.example.connectsit.navigation.ScreenB
import com.example.connectsit.navigation.ScreenD
import com.example.connectsit.navigation.ScreenN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentOptions(navController: NavController) {
    val context = LocalContext.current
    val Bluish = Color(0xFF523EC8)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val courseName = sharedPref.getString("courseName", "") ?: ""
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
                        courseName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ScreenD) }) {
                        Icon(
                            tint = Color.White,
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = { //LOGOUT OPTION YAHA DENA HAI

                    IconButton(onClick = { navController.navigate(ScreenA) }) {
                        Icon(tint = Color.White,

                            imageVector = Icons.Default.Logout,
                            contentDescription = "LOG OUT")
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
            UploadChoices("notes", navController)
            UploadChoices("other", navController)
        }
    }
}

@Composable
fun UploadChoices(category: String, navController: NavController) {
    val context = LocalContext.current
    val Bluish = Color(0xFF523EC8)
    val Buttoncolo = Color(0xFF2A1C9F)
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val courseName = sharedPref.getString("courseName", "") ?: ""

    Row(modifier = Modifier
        .size(width = 400.dp, height = 220.dp)
        .padding(horizontal = 20.dp, vertical = 20.dp)
        .background(color = Bluish, shape = RoundedCornerShape(20.dp))
    ) {
        val imageRes = when (category) {
            "notices" -> R.drawable.notices
            "notes" -> R.drawable.notes
            "other" -> R.drawable.other
            else -> R.drawable.notices // Default image
        }
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "info",
            modifier = Modifier
                .padding(horizontal = 17.dp, vertical = 25.dp)
                .clip(RoundedCornerShape(20))
        )
        Column {
            Text(
                text = category.uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier.padding(horizontal = 17.dp, vertical = 30.dp)
            )
            Button(
                onClick = {
                    with(sharedPref.edit()) {
                        putString("courseName", courseName)
                        putString("category", category)
                        apply()
                    }
                    navController.navigate(ScreenN)
                },
                modifier = Modifier.padding(horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Buttoncolo)
            ) {
                Text(text = "VIEW")
            }
        }
    }
}