package com.example.connectsit

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.connectsit.navigation.*
import com.example.connectsit.ui.model.Enterers
import com.example.connectsit.ui.screens.admin.AdminPortalScreen
import com.example.connectsit.ui.screens.admin.details.StudentDetails
import com.example.connectsit.ui.screens.admin.details.TeacherDetails
import com.example.connectsit.ui.screens.admin.details.manage.ManageDeterminerScreen
import com.example.connectsit.ui.screens.admin.details.manage.ManageStudent
import com.example.connectsit.ui.screens.admin.details.manage.ManageTeacher
import com.example.connectsit.ui.screens.auth.login.LoginScreen
import com.example.connectsit.ui.screens.auth.StudentTeacherDeterminerScreen
import com.example.connectsit.ui.screens.auth.login.LoginViewModel
import com.example.connectsit.ui.screens.student.PdfScreen
import com.example.connectsit.ui.screens.student.StudentOptions
import com.example.connectsit.ui.screens.student.StudentPortalScreen
import com.example.connectsit.ui.screens.teacher.AboutPage
import com.example.connectsit.ui.screens.teacher.TeacherOptions
import com.example.connectsit.ui.screens.teacher.TeacherPortalScreen
import com.example.connectsit.ui.screens.teacher.UploadScreen
import com.example.connectsit.ui.screens.teacher.ViewUploaded
import com.example.connectsit.ui.util.viewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

                createNotificationChannel()
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission denied, notify the user accordingly
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }


        requestNotificationPermission()

        setContent {

            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = ScreenA
            ) {
                // Your composable routes as they are...
                composable<ScreenA> {
                    StudentTeacherDeterminerScreen(
                        handleNavigation = { enterer ->
                            navController.navigate(ScreenB(enterer.value))
                        }
                    )
                }

                composable<ScreenB> {
                    val loginViewModel = viewModel<LoginViewModel>(
                        factory = viewModelFactory {
                            LoginViewModel(
                                MyApplication.appModule.auth,
                                MyApplication.appModule.db
                            )
                        }
                    )

                    val args = it.toRoute<ScreenB>()
                    val enterer = when (args.name) {
                        "STUDENT" -> Enterers.STUDENT
                        "TEACHER" -> Enterers.TEACHER
                        else -> Enterers.ADMIN
                    }
                    loginViewModel.setUserType(enterer)

                    LoginScreen(
                        loginState = loginViewModel.loginState,
                        handleLogin = { loginModel ->
                            loginViewModel.loginUser(
                                username = loginModel.username,
                                password = loginModel.password,
                                enterer = loginViewModel.loginState.userType,
                                onSuccess = { user ->
                                    when (user) {
                                        Enterers.TEACHER -> {
                                            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                            with (sharedPref.edit()) {
                                                putString("username", loginModel.username)
                                                apply()
                                            }
                                            navController.navigate(ScreenC) {
                                                popUpTo(ScreenA) { inclusive = true }
                                            }
                                        }

                                        Enterers.STUDENT ->  {
                                            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                            with (sharedPref.edit()) {
                                                putString("username", loginModel.username)
                                                apply()
                                            }
                                            navController.navigate(ScreenD){
                                                popUpTo(ScreenA) { inclusive = true }
                                            }
                                        }
                                        Enterers.ADMIN -> navController.navigate(ScreenE) {
                                            popUpTo(ScreenA) {
                                                inclusive = true
                                            }
                                        }
                                        else -> {
                                            loginModel.onLoginErrorCallback(true)
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Login failed.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                onFailure = {
                                    loginModel.onLoginErrorCallback(true)
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Login failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    )
                }


                composable<ScreenC> {
                    TeacherPortalScreen(navController)
                }

                composable<ScreenD> {
                    StudentPortalScreen(navController)
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
                composable(ScreenK.UploadScreen) { backStackEntry ->
                    val category = backStackEntry.arguments?.getString("category")
                    Log.d("NavHost", "Navigating to UploadScreen with category: $category")
                    UploadScreen(navController = navController, category = category ?: "others")
                }
                composable<ScreenL> {
                    TeacherOptions(navController)
                }
                composable<ScreenM> {
                    StudentOptions(navController)
                }
                composable<ScreenN> {
                    PdfScreen(navController)
                }
                composable<ScreenO> {
                    AboutPage(navController)
                }
                composable<ScreenP> {
                    ViewUploaded(navController)
                }
            }
        }
    }

    //  Function to request notification permission if the app is targeting Android 13+
    private fun requestNotificationPermission() {
        // Check if the permission is already granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission is granted
                    createNotificationChannel()
                }
                else -> {
                    // Request the permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {

            createNotificationChannel()
        }
    }


    //  notification channel
    private fun createNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "YourNotificationChannel"
                val descriptionText = "Channel for your app notifications"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                    description = descriptionText
                }
                val notificationManager: NotificationManager =
                    ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        } catch (e: SecurityException) {
            Log.e("Notification", "Failed to create notification channel: ${e.message}")

        }
    }

}
