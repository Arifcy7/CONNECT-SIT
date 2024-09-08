package com.example.connectsit

import android.app.Application
import com.example.connectsit.data.di.AppModuleImpl
import com.example.connectsit.data.model.AppModule
import com.google.firebase.FirebaseApp

class MyApplication : Application() {

    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this) // Initialize Firebase
        appModule = AppModuleImpl()
    }
}