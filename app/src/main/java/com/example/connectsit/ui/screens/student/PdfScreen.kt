package com.example.connectsit.ui.screens.student

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.connectsit.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

data class PdfFile(val name: String, val downloadUrl: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfScreen(navController: NavController) {
    val Bluish = Color(0xFF523EC8)
    val context = LocalContext.current
    var pdfList by remember { mutableStateOf<List<PdfFile>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        val courseName = getCourseName(context)
        val category = getCategory(context)
        pdfList = fetchPdfList(courseName, category)
        isLoading = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Bluish,
                    titleContentColor = Color.White,
                ),
                title = { Text("PDF LIST") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(color = Color.Black)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (pdfList.isEmpty()) {
                Text(
                    "No PDFs available for this course and category.",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            } else {
                LazyColumn {
                    items(pdfList) { pdf ->
                        PdfListItem(pdf = pdf)
                    }
                }
            }
        }
    }
}

@Composable
fun PdfListItem(pdf: PdfFile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Implement PDF viewing or downloading */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.PictureAsPdf,
            contentDescription = "PDF Icon",
            tint = Color.Red
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = pdf.name)
    }
}

private fun getCourseName(context: Context): String {
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    return sharedPref.getString("courseName", "") ?: ""
}

private fun getCategory(context: Context): String {
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    return sharedPref.getString("category", "") ?: ""
}

private suspend fun fetchPdfList(courseName: String, category: String): List<PdfFile> {
    val storage: FirebaseStorage = Firebase.storage
    val storageRef: StorageReference = storage.reference.child("pdfs/$courseName/$category")

    return try {
        val result = storageRef.listAll().await()
        result.items.map { item ->
            PdfFile(
                name = item.name,
                downloadUrl = item.downloadUrl.await().toString()

            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

