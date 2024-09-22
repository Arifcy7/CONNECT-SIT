package com.example.connectsit.navigation

import kotlinx.serialization.Serializable

//display all screens here

// StudentTeacherDeterminerScreen
@Serializable
object ScreenA

//LoginScreen
@Serializable
data class ScreenB(
    val name: String?
)

//TeacherPortalScreen
@Serializable
data object ScreenC

//StudentPortalScreen
@Serializable
data object ScreenD

//AdminPortalScreen
@Serializable
data object ScreenE

//StudentDetails
@Serializable
object ScreenF

@Serializable
object ScreenG

@Serializable
object ScreenH

//student manage
@Serializable
object ScreenI

//teacher manage
@Serializable
object ScreenJ

//upload screen
@Serializable
object ScreenK

//upload option for teacher
@Serializable
object ScreenL

//upload option for student
@Serializable
object ScreenM

//pdf screen
@Serializable
object ScreenN