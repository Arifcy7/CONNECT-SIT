package com.example.connectsit.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

data class PdfFile(val name: String, val downloadUrl: String)

// Function to fetch the list of PDF files from Firebase Storage
suspend fun fetchPdfList(courseName: String, category: String): List<PdfFile> {
    Log.d("Utils", "Attempting to fetch PDFs from path: pdfs/$courseName/$category")
    val storage: FirebaseStorage = Firebase.storage
    val storageRef = storage.reference.child("pdfs/$courseName/$category")

    return try {
        val result = storageRef.listAll().await()
        Log.d("Utils", "Successfully listed ${result.items.size} items")
        result.items.map { item ->
            PdfFile(
                name = item.name,
                downloadUrl = item.downloadUrl.await().toString()
            )
        }
    } catch (e: Exception) {
        Log.e("Utils", "Error fetching PDF list", e)
        throw e
    }
}

suspend fun openPdfViewer(context: Context, pdfUrl: String, fileName: String) {
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "$fileName.pdf")

    if (!file.exists()) {
        // If file doesn't exist locally, download it
        downloadPdf(context, pdfUrl, fileName)
    }

    // Open the local file
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    try {
        startActivity(context, intent, null)
        Toast.makeText(context, "Opening PDF...", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Log.e("PdfViewer", "Error opening PDF viewer", e)
        Toast.makeText(context, "Error opening PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
    }
}

// Updated function to download PDF
suspend fun downloadPdf(context: Context, pdfUrl: String, fileName: String) {
    withContext(Dispatchers.IO) {
        try {
            val url = URL(pdfUrl)
            val connection = url.openConnection()
            connection.connect()

            val inputStream = connection.getInputStream()
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "$fileName.pdf")

            FileOutputStream(file).use { output ->
                inputStream.use { input ->
                    input.copyTo(output)
                }
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Download successful", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("Utils", "Error downloading PDF", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error downloading PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
// Function to get the course name from Shared Preferences
fun getCourseName(context: Context): String {
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    return sharedPref.getString("courseName", "") ?: ""
}

// Function to get the category from Shared Preferences
fun getCategory(context: Context): String {
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    return sharedPref.getString("category", "") ?: ""
}
