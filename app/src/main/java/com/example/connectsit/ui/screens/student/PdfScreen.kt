package com.example.connectsit.ui.screens.student

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
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
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = Unit) {
        val courseName = getCourseName(context)
        val category = getCategory(context)
        Log.d("PdfScreen", "Fetching PDFs for course: $courseName, category: $category")
        try {
            pdfList = fetchPdfList(courseName, category)
            Log.d("PdfScreen", "Fetched ${pdfList.size} PDFs")
            if (pdfList.isEmpty()) {
                errorMessage = "No PDFs found for this course and category."
            }
        } catch (e: Exception) {
            Log.e("PdfScreen", "Error fetching PDF list", e)
            errorMessage = "Error loading PDFs: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
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
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                errorMessage != null -> Text(
                    errorMessage!!,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
                pdfList.isEmpty() -> Text(
                    "No PDFs available for this course and category.",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
                else -> LazyColumn {
                    items(pdfList) { pdf ->
                        PdfListItem(pdf = pdf,context=context)
                    }
                }
            }
        }
    }
}

@Composable
fun PdfListItem(pdf: PdfFile,context: Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { openPdfViewer(context, pdf.downloadUrl) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.PictureAsPdf,
            contentDescription = "PDF Icon",
            tint = Color.Red
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = pdf.name, color = Color.White)
    }
}
private fun openPdfViewer(context: Context, pdfUrl: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.parse(pdfUrl), "application/pdf")
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    try {
        startActivity(context, intent, null)
        Toast.makeText(context, "Opening...", Toast.LENGTH_SHORT).show()

    } catch (e: Exception) {
        Log.e("PdfViewer", "Error opening PDF viewer", e)
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
    Log.d("PdfScreen", "Attempting to fetch PDFs from path: pdfs/$courseName/$category")
    val storage: FirebaseStorage = Firebase.storage
    val storageRef: StorageReference = storage.reference.child("pdfs/$courseName/$category")

    return try {
        val result = storageRef.listAll().await()
        Log.d("PdfScreen", "Successfully listed ${result.items.size} items")
        result.items.map { item ->
            PdfFile(
                name = item.name,
                downloadUrl = item.downloadUrl.await().toString()
            )
        }
    } catch (e: Exception) {
        Log.e("PdfScreen", "Error fetching PDF list", e)
        throw e
    }
}