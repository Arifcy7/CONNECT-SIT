package com.example.connectsit

import android.app.Application
import com.example.connectsit.data.di.AppModuleImpl
import com.example.connectsit.data.model.AppModule
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyApplication: Application() {

    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this) // Initialize Firebase
        appModule = AppModuleImpl()
    }
}