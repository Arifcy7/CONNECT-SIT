package com.example.connectsit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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
import com.example.connectsit.navigation.ScreenK
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
import com.example.connectsit.ui.screens.student.StudentPortalScreen
import com.example.connectsit.ui.screens.teacher.TeacherPortalScreen
import com.example.connectsit.ui.screens.teacher.UploadScreen
import com.example.connectsit.ui.util.viewModelFactory

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
                                email = loginModel.email,
                                password = loginModel.password,
                                enterer = loginViewModel.loginState.userType,
                                onSuccess = { user ->
                                    when (user) {
                                        Enterers.TEACHER -> navController.navigate(ScreenC) {
                                            //removing login from back stack
                                            popUpTo(ScreenA) {
                                                inclusive = true
                                            }
                                        }
                                        Enterers.STUDENT -> navController.navigate(ScreenD) {
                                            popUpTo(ScreenA) {
                                                inclusive = true
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
                composable<ScreenK> {
                    UploadScreen(navController)
                }
            }
        }
    }
}
