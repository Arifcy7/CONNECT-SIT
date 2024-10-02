package com.example.connectsit.ui.screens.admin.details.manage

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.connectsit.navigation.ScreenH
import com.example.connectsit.navigation.ScreenO


data class Teacher(
    val id: String,
    val name: String,
    val email: String,
    val username: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTeacher(navController: NavController) {
    val Bluish = Color(0xFF523EC8)
    var teachers by remember { mutableStateOf<List<Teacher>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        fetchTeachers { fetchedTeachers, errorMessage ->
            teachers = fetchedTeachers
            error = errorMessage
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Teachers") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Bluish, titleContentColor = Color.White),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color.Black)
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                error != null -> Text(error!!, color = Color.Red, modifier = Modifier.align(Alignment.Center))
                teachers.isEmpty() -> Text("No teachers found", modifier = Modifier.align(Alignment.Center))
                else -> TeacherList(teachers) { deletedTeacher ->
                    deleteTeacher(deletedTeacher, context) { success ->
                        if (success) {
                            teachers = teachers.filter { it.id != deletedTeacher.id }
                            Toast.makeText(context, "Teacher deleted", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeacherList(teachers: List<Teacher>, onDelete: (Teacher) -> Unit) {
    LazyColumn {
        items(teachers) { teacher ->
            TeacherItem(teacher, onDelete)
        }
    }
}

@Composable
fun TeacherItem(teacher: Teacher, onDelete: (Teacher) -> Unit) {
    val Blu = Color(0xFF523EC8)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp) ,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = teacher.name, style = MaterialTheme.typography.titleMedium)
                Text(text = teacher.email, style = MaterialTheme.typography.bodyMedium)
                Text(text = "Username: ${teacher.username}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onDelete(teacher) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete teacher")
            }
        }
    }
}

private fun fetchTeachers(callback: (List<Teacher>, String?) -> Unit) {
    Firebase.firestore.collection("Teachers")
        .get()
        .addOnSuccessListener { result ->
            val teacherList = result.documents.mapNotNull { doc ->
                doc.data?.let { data ->
                    Teacher(
                        id = doc.id,
                        name = data["name"] as? String ?: "",
                        email = data["email"] as? String ?: "",
                        username = data["username"] as? String ?: ""
                    )
                }
            }
            callback(teacherList, null)
        }
        .addOnFailureListener { exception ->
            callback(emptyList(), "Error fetching teachers: ${exception.message}")
        }
}

private fun deleteTeacher(teacher: Teacher, context: android.content.Context,callback: (Boolean) -> Unit) {
    Firebase.firestore.collection("Teachers").document(teacher.id)
        .delete()
        .addOnSuccessListener {
            callback(true)
            Toast.makeText(context, "Teacher deleted", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { exception ->
            println("Error deleting teacher: ${exception.message}")
            callback(false)
            Toast.makeText(context, "Not able to delete", Toast.LENGTH_SHORT).show()
        }
}
