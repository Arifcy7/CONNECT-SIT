package com.example.connectsit.ui.model.login

import com.example.connectsit.ui.model.Enterers

data class LoginState(
    val userType: Enterers = Enterers.ADMIN,
    val showLoadingScreen: Boolean = false,
)
