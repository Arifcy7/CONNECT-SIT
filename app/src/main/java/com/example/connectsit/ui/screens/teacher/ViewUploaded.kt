package com.example.connectsit.ui.screens.teacher

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
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
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

data class PdfFile(val name: String, val downloadUrl: String, val category: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewUploaded(navController: NavController) {
    val Bluish = Color(0xFF523EC8)
    val context = LocalContext.current
    var pdfList by remember { mutableStateOf<List<PdfFile>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val cat = getCategory(context)
    LaunchedEffect(key1 = Unit) {
        val courseName = getCourseName(context)
        val category = getCategory(context)
        Log.d("ViewUploaded", "Fetching PDFs for course: $courseName,category: $category")
        try {
            pdfList = fetchAllPdfList(courseName,category)
            Log.d("ViewUploaded", "Fetched ${pdfList.size} PDFs")
            if (pdfList.isEmpty()) {
                errorMessage = "Nothing found for this course."
            }
        } catch (e: Exception) {
            Log.e("ViewUploaded", "Error fetching PDF list", e)
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
                title = { Text("UPLOADED ${cat.uppercase()}") },
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
                    "Nothing uploaded for this course.",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
                else -> LazyColumn {
                    items(pdfList) { pdf ->
                        UploadedPdfListItem(
                            pdf = pdf,
                            onDelete = {
                                deletePdf(pdf, context) {
                                    // Refresh the list after deletion
                                    pdfList = pdfList.filter { it != pdf }
                                }
                            },
                            onView = {
                                openPdfViewer(context, pdf.downloadUrl)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UploadedPdfListItem(pdf: PdfFile, onDelete: () -> Unit, onView: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = pdf.name, color = Color.White)
            Text(text = "Category: ${pdf.category}", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = onView) {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = "View PDF",
                tint = Color.White
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete notes",
                tint = Color.Red
            )
        }
    }
}

private fun deletePdf(pdf: PdfFile, context: Context, onDeleteSuccess: () -> Unit) {
    val storage = Firebase.storage
    val courseName = getCourseName(context)
    val fileRef = storage.reference.child("pdfs/$courseName/${pdf.category}/${pdf.name}")

    fileRef.delete().addOnSuccessListener {
        Log.d("ViewUploaded", "PDF deleted successfully: ${pdf.name}")
        onDeleteSuccess()
        Toast.makeText(context, "PDF deleted successfully", Toast.LENGTH_SHORT).show()
    }.addOnFailureListener { e ->
        Log.e("ViewUploaded", "Error deleting PDF: ${pdf.name}", e)
        Toast.makeText(context, "Error deleting PDF", Toast.LENGTH_SHORT).show()
    }
}

private fun openPdfViewer(context: Context, pdfUrl: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.parse(pdfUrl), "application/pdf")
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    try {
        startActivity(context, intent, null)
        Toast.makeText(context, "Opening PDF...", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Log.e("PdfViewer", "Error opening PDF viewer", e)
        Toast.makeText(context, "Error opening PDF. No PDF viewer app found.", Toast.LENGTH_LONG).show()
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
private suspend fun fetchAllPdfList(courseName: String, category: String): List<PdfFile> {
    Log.d("ViewUploaded", "Attempting to fetch all PDFs for course: $courseName, category: $category")
    val storage = Firebase.storage
    val categoryRef = storage.reference.child("pdfs/$courseName/$category")

    return try {
        val result = categoryRef.listAll().await()
        result.items.map { item ->
            PdfFile(
                name = item.name,
                downloadUrl = item.downloadUrl.await().toString(),
                category = category
            )
        }
    } catch (e: Exception) {
        Log.e("ViewUploaded", "Error fetching PDF list", e)
        throw e
    }
}