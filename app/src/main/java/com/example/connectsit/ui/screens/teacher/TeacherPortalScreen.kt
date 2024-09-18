package com.example.connectsit.ui.screens.teacher



import android.content.ContentValues.TAG
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.tasks.await


data class Courses(
    var id: String,
    var courseName: String,
    var teacherUsername: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherPortalScreen(navController: NavController) {
    val Bluish = Color(0xFF523EC8)
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
                .background(color = Color.Black)
        ) {
            Spacer(modifier = Modifier.size(40.dp))
            CoursesList()
        }
    }
}

suspend fun GetData(): List<Courses> {
    val db = FirebaseFirestore.getInstance()
    return try {
        db.collection("Courses").get().await()
            .map { it: QueryDocumentSnapshot ->
                Courses(
                    id = it.id,
                    courseName = it.getString("courseName")!!,
                    teacherUsername = it.getString("teacherUsername")!!
                )
            }
    } catch (e: FirebaseFirestoreException) {
        Log.d("TAG", "getDataFromFirestore: $e")
        emptyList()
    }
}

@Composable
fun CoursesList() {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var coursesList by remember { mutableStateOf<List<Courses>>(emptyList()) }

    LaunchedEffect(Unit) {
        val data = GetData()
        coursesList = data
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(coursesList) { course ->
                Text(text = course.courseName, modifier = Modifier.padding(16.dp), color = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun CoursesListPreview() {
    TeacherPortalScreen(navController = NavController(context = LocalContext.current))
}