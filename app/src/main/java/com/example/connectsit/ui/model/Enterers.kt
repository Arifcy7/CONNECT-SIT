package com.example.connectsit.ui.model

enum class Enterers(val value: String) {
    STUDENT("STUDENT"),
    TEACHER("TEACHER"),
    ADMIN("ADMIN"),
}

fun getEnterer(entererType: String): Enterers? {
    return when (entererType.uppercase()) {
        "STUDENT" -> Enterers.STUDENT
        "TEACHER" -> Enterers.TEACHER
        "ADMIN" -> Enterers.ADMIN
        else -> null
    }
}
