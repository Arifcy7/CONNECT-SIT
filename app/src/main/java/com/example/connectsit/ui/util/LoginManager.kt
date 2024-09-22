package com.example.connectsit.ui.util

import android.content.Context
import android.content.SharedPreferences
import com.example.connectsit.ui.model.Enterers

class LoginManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

    fun saveLoginState(username: String, userType: Enterers) {
        sharedPreferences.edit().apply {
            putBoolean("isLoggedIn", true)
            putString("username", username)
            putString("userType", userType.name)
            apply()
        }
    }

    fun clearLoginState() {
        sharedPreferences.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean("isLoggedIn", false)

    fun getUsername(): String? = sharedPreferences.getString("username", null)

    fun getUserType(): Enterers? {
        val userTypeString = sharedPreferences.getString("userType", null)
        return userTypeString?.let { Enterers.valueOf(it) }
    }
}
