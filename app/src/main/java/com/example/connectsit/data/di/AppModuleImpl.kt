package com.example.connectsit.data.di

import com.example.connectsit.data.model.AppModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppModuleImpl : AppModule {
    override val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

}