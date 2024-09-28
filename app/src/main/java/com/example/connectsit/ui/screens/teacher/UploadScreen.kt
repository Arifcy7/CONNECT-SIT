package com.example.connectsit.ui.screens.teacher

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.connectsit.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(navController: NavController, category: String) {
    Log.d("UploadScreen", "Received category: $category")
    val Bluish = Color(0xFF523EC8)
    val context = LocalContext.current
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var uploadProgress by remember { mutableStateOf(0f) }
    var isUploading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var fileName by remember { mutableStateOf("") }
    var showNamingDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
        errorMessage = null
        if (uri != null) {
            showNamingDialog = true
        }
    }

    if (showNamingDialog) {
        AlertDialog( modifier = Modifier.background(color = Color.Black),
            onDismissRequest = { showNamingDialog = false },
            title = { Text("Name Your File") },
            text = {
                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("File Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (fileName.isNotBlank()) {
                            showNamingDialog = false
                        }
                    }, colors = ButtonDefaults.buttonColors(containerColor = Bluish, contentColor = Color.White)
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton( colors = ButtonDefaults.buttonColors(containerColor = Bluish, contentColor = Color.White),
                    onClick = {
                        showNamingDialog = false
                        selectedFileUri = null
                        fileName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Bluish,
                    titleContentColor = Color.White,
                ),
                title = { Text("UPLOAD HERE") },
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_2),
                contentDescription = "Upload Pdfs",
                modifier = Modifier.clickable { launcher.launch("application/pdf") }
            )
            Text(
                text = if (selectedFileUri != null) {
                    "PDF Selected: $fileName\nCategory: $category"
                } else {
                    "CLICK TO UPLOAD\nCategory: $category"},
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = {
                    selectedFileUri?.let { uri ->
                        isUploading = true
                        errorMessage = null
                        uploadPdfToFirebase(uri, context, fileName,
                            onProgressUpdate = { progress ->
                                uploadProgress = progress
                                if (progress >= 1f) {
                                    isUploading = false
                                }
                            },
                            onError = { message ->
                                errorMessage = message
                                isUploading = false
                            }
                        )
                    }
                },

                modifier = Modifier.size(width = 180.dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Bluish,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Blue.copy(alpha = 0.5f),
                    disabledContentColor = Color.White.copy(alpha = 0.5f)),
                enabled = selectedFileUri != null && !isUploading && fileName.isNotBlank()
            ) {
                Log.d("crasherror", "UploadScreen: Failed2")
                Text(text = if (isUploading) "UPLOADING..." else "UPLOAD", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isUploading) {
                LinearProgressIndicator(
                    progress = uploadProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(horizontal = 16.dp)
                        .background(color = Color.White),
                    color = Bluish
                )
            }
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

fun uploadPdfToFirebase(
    fileUri: Uri,
    context: Context,
    fileName: String,
    onProgressUpdate: (Float) -> Unit,
    onError: (String) -> Unit
) {
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val courseName = sharedPref.getString("courseName", "") ?: ""
    val category = sharedPref.getString("category", "") ?: ""
    val storage: FirebaseStorage = Firebase.storage
    val storageRef = storage.reference
    val safeFileName = "${fileName.replace("[^a-zA-Z0-9.-]".toRegex(), "_")}.pdf"
    val pdfRef = storageRef.child("pdfs/$courseName/$category/$safeFileName")

    pdfRef.putFile(fileUri)
        .addOnProgressListener { taskSnapshot ->
            val progress = taskSnapshot.bytesTransferred.toFloat() / taskSnapshot.totalByteCount.toFloat()
            onProgressUpdate(progress)
        }
        .addOnSuccessListener {
            onProgressUpdate(1f)
            Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { exception ->
            onProgressUpdate(0f)
            val errorMessage = "Upload failed: ${exception.localizedMessage}"
            onError(errorMessage)
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            Log.d("crasherror", "UploadScreen: Failed3")

        }
}

@Preview
@Composable
fun UploadScreenPreview() {
    // Simulating different categories for preview
    val categories = listOf("notes", "notices", "other")
    Log.d("crasherror", "UploadScreen: Failed4")

    for (category in categories) {
        UploadScreen(navController = NavController(LocalContext.current), category = category)
    }
}



