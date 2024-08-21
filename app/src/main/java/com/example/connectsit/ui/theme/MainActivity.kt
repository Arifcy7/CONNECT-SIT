package com.example.connectsit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.connectsit.ui.screen.LogInScreenRoute
import com.example.connectsit.ui.screen.OptionChoosingScreenRoute
import com.example.connectsit.ui.screen.TeacherScreenRoute
import com.example.connectsit.ui.screen.auth.LoginScreen
import com.example.connectsit.ui.screen.auth.OptionChoosingScreen
import com.example.connectsit.ui.screen.teacher.TeacherPortal

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = OptionChoosingScreenRoute
            ) {
                composable<OptionChoosingScreenRoute> {
                    OptionChoosingScreen(navController)
                }

                composable<LogInScreenRoute> {
                    val args = it.toRoute<LogInScreenRoute>()
                    LoginScreen(enterer = args.name, navController)
                }
                composable<TeacherScreenRoute> {
                    TeacherPortal()
                }
            }
        }
    }
}

//    @Serializable
//    object ScreenA
//
//    @Serializable
//    data class ScreenB(
//        val name: String?
//    )
//    @Serializable
//    data object ScreenC