package com.example.connectsit.data.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface AppModule {
    val auth: FirebaseAuth
    val db: FirebaseFirestore
}