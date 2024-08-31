package com.example.connectsit

import android.os.Bundle
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
import com.example.connectsit.ui.screens.auth.LoginScreen
import com.example.connectsit.ui.screens.student.StudentPortalScreen
import com.example.connectsit.ui.screens.auth.StudentTeacherDeterminerScreen
import com.example.connectsit.ui.screens.admin.AdminPortalScreen
import com.example.connectsit.ui.screens.admin.details.StudentDetails
import com.example.connectsit.ui.screens.admin.details.TeacherDetails
import com.example.connectsit.ui.screens.teacher.TeacherPortalScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = ScreenA
            ) {
                // Moved all screens to its own file to reduce clutter
                composable<ScreenA> {
                    StudentTeacherDeterminerScreen(
                        handleNavigation = { enterer ->
                            navController.navigate(ScreenB(enterer.value))
                        }
                    )
                }

                composable<ScreenB> {
                    val args = it.toRoute<ScreenB>()
                    // based on the argument given determines our Enterer
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
                                Enterers.ADMIN ->{
                                    navController.navigate(ScreenE)
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
                    StudentDetails()
                }
                composable<ScreenG> {
                    TeacherDetails()
                }
            }
        }
    }
}
