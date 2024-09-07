package com.example.connectsit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.connectsit.ui.model.Enterers
import com.example.connectsit.navigation.ScreenA
import com.example.connectsit.navigation.ScreenB
import com.example.connectsit.navigation.ScreenC
import com.example.connectsit.navigation.ScreenD
import com.example.connectsit.navigation.ScreenE
import com.example.connectsit.navigation.ScreenF
import com.example.connectsit.navigation.ScreenG
import com.example.connectsit.navigation.ScreenH
import com.example.connectsit.navigation.ScreenI
import com.example.connectsit.navigation.ScreenJ
import com.example.connectsit.ui.screens.auth.LoginScreen
import com.example.connectsit.ui.screens.student.StudentPortalScreen
import com.example.connectsit.ui.screens.auth.StudentTeacherDeterminerScreen
import com.example.connectsit.ui.screens.admin.AdminPortalScreen
import com.example.connectsit.ui.screens.admin.details.StudentDetails
import com.example.connectsit.ui.screens.admin.details.TeacherDetails
import com.example.connectsit.ui.screens.admin.details.manage.ManageDeterminerScreen
import com.example.connectsit.ui.screens.admin.details.manage.ManageStudent
import com.example.connectsit.ui.screens.admin.details.manage.ManageTeacher
import com.example.connectsit.ui.screens.teacher.TeacherPortalScreen
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // Initialize Firebase
        auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth instance
        db = FirebaseFirestore.getInstance() // Initialize Firestore

        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = ScreenA
            ) {
                composable<ScreenA> {
                    StudentTeacherDeterminerScreen(
                        handleNavigation = { enterer ->
                            navController.navigate(ScreenB(enterer.value))
                        }
                    )
                }

                composable<ScreenB> {
                    val args = it.toRoute<ScreenB>()
                    val enterer = when (args.name) {
                        "STUDENT" -> Enterers.STUDENT
                        "TEACHER" -> Enterers.TEACHER
                        else -> Enterers.ADMIN
                    }
                    LoginScreen(
                        enterer = enterer,
                        handleLogin = { email, password, callback ->
                            loginUser(email, password, enterer) { success ->
                                callback(success) // Invoke the callback with the result
                                if (success) {
                                    when (enterer) {
                                        Enterers.TEACHER -> navController.navigate(ScreenC)
                                        Enterers.STUDENT -> navController.navigate(ScreenD)
                                        Enterers.ADMIN -> navController.navigate(ScreenE)
                                    }
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Login failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    )
                }

                composable<ScreenC> {
                    TeacherPortalScreen()
                }

                composable<ScreenD> {
                    StudentPortalScreen()
                }

                composable<ScreenE> {
                    AdminPortalScreen(navController)
                }

                composable<ScreenF> {
                    StudentDetails(navController)
                }

                composable<ScreenG> {
                    TeacherDetails(navController)
                }
                composable<ScreenH> {
                    ManageDeterminerScreen(navController)
                }
                composable<ScreenI> {
                    ManageStudent(navController)
                }
                composable<ScreenJ> {
                    ManageTeacher(navController)
                }
            }
        }
    }

    private fun loginUser(email: String, password: String, enterer: Enterers, callback: (Boolean) -> Unit) {
        val collection = when (enterer) {
            Enterers.ADMIN -> "Admins"
            Enterers.TEACHER -> "Teachers"
            Enterers.STUDENT -> "Students"
        }

        db.collection(collection)
            .whereEqualTo("email", email)
            .whereEqualTo("password", password) // For a secure app, avoid storing plain passwords; use hashing
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Login success
                    callback(true)
                } else {
                    // Login failed
                    callback(false)
                }
            }
            .addOnFailureListener {
                // Handle error
                callback(false)
            }
    }
}
