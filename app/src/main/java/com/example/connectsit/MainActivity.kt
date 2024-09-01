package com.example.connectsit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.connectsit.ui.model.Enterers
import com.example.connectsit.navigation.ScreenA
import com.example.connectsit.navigation.ScreenB
import com.example.connectsit.navigation.ScreenC
import com.example.connectsit.navigation.ScreenD
import com.example.connectsit.navigation.ScreenE
import com.example.connectsit.navigation.ScreenF
import com.example.connectsit.navigation.ScreenG
import com.example.connectsit.navigation.ScreenH
import com.example.connectsit.ui.screens.auth.LoginScreen
import com.example.connectsit.ui.screens.student.StudentPortalScreen
import com.example.connectsit.ui.screens.auth.StudentTeacherDeterminerScreen
import com.example.connectsit.ui.screens.admin.AdminPortalScreen
import com.example.connectsit.ui.screens.admin.details.StudentDetails
import com.example.connectsit.ui.screens.admin.details.TeacherDetails
import com.example.connectsit.ui.screens.teacher.TeacherPortalScreen
import android.app.Application
import android.widget.Toast
import androidx.navigation.toRoute
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // Initialize Firebase
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
                        handleLogin = { username, password ->
                            when (enterer) {
                                Enterers.TEACHER -> {
                                    navController.navigate(ScreenC)
                                }
                                Enterers.STUDENT -> {
                                    navController.navigate(ScreenD)
                                }
                                Enterers.ADMIN -> { if (username=="apsit" && password=="123")
                                {navController.navigate(ScreenE)}
                                    else
                                    {
                                       Toast.makeText(this@MainActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()

                                    }
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
            }
        }
    }
}
