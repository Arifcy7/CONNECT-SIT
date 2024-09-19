package com.example.connectsit.ui.screens.auth.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connectsit.ui.model.Enterers
import com.example.connectsit.ui.model.getEnterer
import com.example.connectsit.ui.model.login.LoginState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDb: FirebaseFirestore
) : ViewModel() {

    var loginState by mutableStateOf(
        LoginState()
    )

    fun loginUser(
        username: String,
        password: String,
        enterer: Enterers,
        onSuccess: (Enterers?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        setLoadingScreenDialog(true)
        viewModelScope.launch(Dispatchers.IO) {
            val collection = when (enterer) {
                Enterers.ADMIN -> "Admins"
                Enterers.TEACHER -> "Teachers"
                Enterers.STUDENT -> "Students"
            }
            useFirebaseToLogin(
                username = username,
                password = password,
                enterer = enterer,
                collection = collection,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }

    private fun useFirebaseToLogin(
        username: String,
        password: String,
        enterer: Enterers,
        collection: String,
        onSuccess: (Enterers?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        firebaseDb.collection(collection)
            .whereEqualTo("username", username)
            .whereEqualTo(
                "password",
                password
            ) // For a secure app, avoid storing plain passwords; use hashing
            .get()
            .addOnSuccessListener { documents ->
                setLoadingScreenDialog(false)
                if (documents.isEmpty) {
                    onFailure("No Document present")
                    return@addOnSuccessListener
                }
                // Login success returning enterer/collection
                onSuccess(getEnterer(enterer.value))
            }
            .addOnFailureListener {
                setLoadingScreenDialog(false)
                Log.d("Error", "Could not get user from DB")
                onFailure("Error")
                // Handle error
            }
    }

    fun setUserType(user: Enterers) {
        updateLoginState(loginState.copy(userType = user))
    }

    fun setLoadingScreenDialog(flag: Boolean) {
        updateLoginState(loginState.copy(showLoadingScreen = flag))
    }

    private fun updateLoginState(newLoginState: LoginState) {
        loginState = newLoginState
    }
}