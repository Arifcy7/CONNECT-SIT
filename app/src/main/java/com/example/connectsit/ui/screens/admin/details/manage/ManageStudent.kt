package com.example.connectsit.ui.screens.admin.details.manage

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import com.example.connectsit.navigation.ScreenH


data class Student(
    val id: String,
    val name: String,
    val email: String,
    val username: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageStudent(navController: NavController) {
    val Bluish = Color(0xFF523EC8)
    var students by remember { mutableStateOf<List<Student>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        fetchStudents { fetchedStudents, errorMessage ->
            students = fetchedStudents
            error = errorMessage
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Students") },
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
                students.isEmpty() -> Text("No students found", modifier = Modifier.align(Alignment.Center))
                else -> StudentList(students) { deletedStudent ->
                    deleteStudent(deletedStudent, context) { success ->
                        if (success) {
                            students = students.filter { it.id != deletedStudent.id }
                            Toast.makeText(context, "Student deleted", Toast.LENGTH_SHORT).show()
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
fun StudentList(students: List<Student>, onDelete: (Student) -> Unit) {
    LazyColumn {
        items(students) { student ->
            StudentItem(student, onDelete)
        }
    }
}

@Composable
fun StudentItem(student: Student, onDelete: (Student) -> Unit) {
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
                Text(text = student.name, style = MaterialTheme.typography.titleMedium)
                Text(text = student.email, style = MaterialTheme.typography.bodyMedium)
                Text(text = "Username: ${student.username}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onDelete(student) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete student")
            }
        }
    }
}

private fun fetchStudents(callback: (List<Student>, String?) -> Unit) {
    Firebase.firestore.collection("Students")
        .get()
        .addOnSuccessListener { result ->
            val studentList = result.documents.mapNotNull { doc ->
                doc.data?.let { data ->
                    Student(
                        id = doc.id,
                        name = data["name"] as? String ?: "",
                        email = data["email"] as? String ?: "",
                        username = data["username"] as? String ?: ""
                    )
                }
            }
            callback(studentList, null)
        }
        .addOnFailureListener { exception ->
            callback(emptyList(), "Error fetching students: ${exception.message}")
        }
}

private fun deleteStudent(student: Student, context: android.content.Context,callback: (Boolean) -> Unit) {
    Firebase.firestore.collection("Students").document(student.id)
        .delete()
        .addOnSuccessListener {
            callback(true)
            Toast.makeText(context, "Student deleted", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { exception ->
            println("Error deleting student: ${exception.message}")
            callback(false)
            Toast.makeText(context, "Not able to delete", Toast.LENGTH_SHORT).show()
        }
}