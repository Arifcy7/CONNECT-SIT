package com.example.connectsit.ui.model

import com.example.connectsit.ui.model.Enterers.ADMIN
import com.example.connectsit.ui.model.Enterers.STUDENT
import com.example.connectsit.ui.model.Enterers.TEACHER

enum class Enterers(val value: String) {
    STUDENT("STUDENT"),
    TEACHER("TEACHER"),
    ADMIN("ADMIN"),

}

fun getEnterer(entererType: String): Enterers? {
    return when (entererType.uppercase()) {
        "STUDENT" -> STUDENT
        "TEACHER" -> TEACHER
        "ADMIN" -> ADMIN
        else -> null
    }
}