package com.example.connectsit.ui.screens.student

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.connectsit.navigation.ScreenM
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await


data class Courses(
    var id: String,
    var courseName: String,
    var teacherUsername: String,
    var studentUsernames: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentPortalScreen(navController: NavController) {
    val Bluish = Color(0xFF523EC8)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

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
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription ="LOG OUT",
                            tint = Color.White)
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
                .background(color = Color.Black)
        ) {
            Spacer(modifier = Modifier.size(40.dp))
            CoursesList(context,navController)
        }
    }
}

suspend fun GetData(studentUsername: String): List<Courses> {
    val db = FirebaseFirestore.getInstance()
    return try {
        db.collection("Courses")
            .whereArrayContains("studentUsernames", studentUsername)
            .get()
            .await()
            .map { document ->
                Courses(
                    id = document.id,
                    courseName = document.getString("courseName") ?: "",
                    teacherUsername = document.getString("teacherUsername") ?: "",
                    studentUsernames = document.get("studentUsernames") as? List<String> ?: emptyList()
                )
            }
    } catch (e: FirebaseFirestoreException) {
        Log.d("TAG", "getDataFromFirestore: $e")
        emptyList()
    }
}

@Composable
fun CoursesList(context: Context,navController: NavController) {
    var isLoading by remember { mutableStateOf(true) }
    var coursesList by remember { mutableStateOf<List<Courses>>(emptyList()) }

    // Retrieve username from SharedPreferences
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val username = sharedPref.getString("username", "") ?: ""

    LaunchedEffect(Unit) {
        if (username.isNotEmpty()) {
            val data = GetData(username)
            coursesList = data
            isLoading = false
        } else {
            // Handle case where username is not available
            isLoading = false
        }
    }

    when {
        isLoading -> {
            CircularProgressIndicator(color = Color.White)
        }
        coursesList.isEmpty() -> {
            Text(
                text = "No courses available",
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(coursesList) { course ->
                    Divider(color = Color.White)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val sharedPref =
                                    context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                                with(sharedPref.edit()) {
                                    putString("courseName", course.courseName)
                                    apply()
                                }
                                navController.navigate(ScreenM)
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = course.courseName,
                            modifier = Modifier
                                .padding(16.dp),
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "see course",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)

                        )

                    }
                    Divider(color = Color.White)

                }
            }
        }
    }
}

