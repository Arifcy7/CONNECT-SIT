package com.example.connectsit.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
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

// Function to open the PDF viewer
fun openPdfViewer(context: Context, pdfUrl: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.parse(pdfUrl), "application/pdf")
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    try {
        startActivity(context, intent, null)
        Toast.makeText(context, "Opening...", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Log.e("PdfViewer", "Error opening PDF viewer", e)
        Toast.makeText(context, "Error opening PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
    }
}

// Function to download a PDF from a URL
suspend fun downloadPdf(context: Context, pdfUrl: String, fileName: String) {
    try {
        // Create a URL object from the pdfUrl string
        val url = URL(pdfUrl)
        // Create a connection to the URL
        val connection = url.openConnection()
        connection.connect()

        // Get the input stream from the connection
        val inputStream = connection.getInputStream()
        // Create a file in the external storage directory
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "$fileName.pdf")

        // Create a file output stream to write to the file
        val outputStream = FileOutputStream(file)

        // Write the input stream to the output stream
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        Toast.makeText(context, "Download successful: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Log.e("Utils", "Error downloading PDF", e)
        Toast.makeText(context, "Error downloading PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
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
