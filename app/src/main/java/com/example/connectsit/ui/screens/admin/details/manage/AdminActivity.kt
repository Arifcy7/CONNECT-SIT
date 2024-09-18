package com.example.connectsit.ui.screens.admin.details.manage

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.QuerySnapshot
import android.util.Log
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

class AdminActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()

        setContent {
            AdminScreen(db)
        }
    }

    @Composable
    fun AdminScreen(db: FirebaseFirestore) {
        var courseName by remember { mutableStateOf("") }
        var teacherUsername by remember { mutableStateOf("") }
        var studentUsernames by remember { mutableStateOf("") }
        var feedbackMessage by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.Black),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = courseName,
                onValueChange = { courseName = it },
                label = { Text("Course Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Blue,
                    unfocusedIndicatorColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = teacherUsername,
                onValueChange = { teacherUsername = it },
                label = { Text("Teacher Username") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Blue,
                    unfocusedIndicatorColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = studentUsernames,
                onValueChange = { studentUsernames = it },
                label = { Text("Student Usernames (comma separated)") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Blue,
                    unfocusedIndicatorColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    isLoading = true
                    val studentsList = studentUsernames.split(",").map { it.trim() }
                    if (courseName.isNotEmpty() && teacherUsername.isNotEmpty() && studentsList.isNotEmpty()) {
                        checkTeacherAndStudents(courseName, teacherUsername, studentsList, context) { isValid, message ->
                            if (isValid) {
                                createCourse(courseName, teacherUsername, studentsList, context)
                            } else {
                                feedbackMessage = message
                            }
                            isLoading = false
                        }
                    } else {
                        feedbackMessage = "Please fill out all fields"
                        isLoading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Create Course")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = feedbackMessage, color = Color.Red)
        }
    }

    private fun checkTeacherAndStudents(courseName: String, teacherUsername: String, studentUsernames: List<String>, context: Context, callback: (Boolean, String) -> Unit) {

        db.collection("Teachers").whereEqualTo("username", teacherUsername).get()
            .addOnSuccessListener { teacherSnapshot ->
                if (teacherSnapshot.isEmpty) {
                    callback(false, "Teacher Username does not exist")
                } else {
                    // Check if all students exist
                    val studentChecks = studentUsernames.map { username ->
                        db.collection("Students").whereEqualTo("username", username).get()
                    }


                    Tasks.whenAllSuccess<QuerySnapshot>(studentChecks).addOnSuccessListener { snapshots ->
                        if (snapshots.any { it.isEmpty }) {
                            callback(false, "Some student usernames do not exist")
                        } else {
                            callback(true, "")
                        }
                    }
                }
            }
            .addOnFailureListener {
                Log.e("ManageCourses", "Failed to check teacher", it)
                Toast.makeText(context, "Failed to check teacher", Toast.LENGTH_SHORT).show()
                callback(false, "Error checking teacher")
            }
    }

    private fun createCourse(courseName: String, teacherUsername: String, studentUsernames: List<String>, context: Context) {
        val courseId = db.collection("Courses").document().id
        val course = hashMapOf(
            "courseName" to courseName,
            "adminId" to FirebaseAuth.getInstance().currentUser?.uid,
            "teacherUsername" to teacherUsername,
            "studentUsernames" to studentUsernames
        )

        db.collection("Courses").document(courseId).set(course)
            .addOnSuccessListener {
                Toast.makeText(context, "Course Created", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to Create Course", Toast.LENGTH_SHORT).show()
            }
    }
}
