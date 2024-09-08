package com.example.connectsit.ui.model.login

import com.example.connectsit.ui.model.Enterers

typealias showErrorAnimation = Boolean

data class LoginModel(
    val email: String,
    val password: String,
    val userType: Enterers,
    val onLoginErrorCallback: (showErrorAnimation) -> Unit
)
