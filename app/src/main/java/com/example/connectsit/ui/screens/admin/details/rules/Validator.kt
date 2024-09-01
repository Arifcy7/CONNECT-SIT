package com.example.connectsit.ui.screens.admin.details.rules

object Validator {

    // Function to validate name
    fun validateName(name: String): String? {
        return if (name.isBlank()) {
            "Name cannot be empty"
        } else if (name.length < 3) {
            "Name must be at least 3 characters long"
        } else null
    }

    // Function to validate username
    fun validateUsername(username: String): String? {
        return if (username.isBlank()) {
            "Username cannot be empty"
        } else if (username.length < 4) {
            "Username must be at least 4 characters long"
        } else null
    }

    // Function to validate password
    fun validatePassword(password: String): String? {
        return if (password.isBlank()) {
            "Password cannot be empty"
        } else if (password.length < 8) {
            "Password must be at least 8 characters long"
        } else null
    }

    // Function to validate email
    fun validateEmail(email: String): String? {
        return if (email.isBlank()) {
            "Email cannot be empty"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Enter a valid email address"
        } else null
    }
}
