package com.example.connectsit.ui.screens.teacher

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.connectsit.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(navController: NavController) {
    val Bluish = Color(0xFF523EC8)
    val context = LocalContext.current
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var uploadProgress by remember { mutableStateOf(0f) }
    var isUploading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
        errorMessage = null // Clear any previous error messages
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_2),
                contentDescription = "Upload Image",
                modifier = Modifier.clickable { launcher.launch("application/pdf") }
            )
            Text(
                text = if (selectedFileUri != null) "PDF Selected" else "CLICK TO UPLOAD",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = {
                    selectedFileUri?.let { uri ->
                        isUploading = true
                        errorMessage = null // Clear any previous error messages
                        uploadPdfToFirebase(uri, context,
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
                colors = ButtonDefaults.buttonColors(containerColor = Bluish),
                enabled = selectedFileUri != null && !isUploading
            ) {
                Text(text = if (isUploading) "UPLOADING..." else "UPLOAD")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isUploading) {
                LinearProgressIndicator(
                    progress = uploadProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(horizontal = 16.dp),
                    color = Color.Blue
                )
            }
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

fun uploadPdfToFirebase(
    fileUri: Uri,
    context: Context,
    onProgressUpdate: (Float) -> Unit,
    onError: (String) -> Unit
) {
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val courseName = sharedPref.getString("courseName", "") ?: ""
    val category = sharedPref.getString("category", "") ?: ""
    val storage: FirebaseStorage = Firebase.storage
    val storageRef = storage.reference
    val fileName = "${UUID.randomUUID()}.pdf"
    val pdfRef = storageRef.child("pdfs/$category/$courseName/$fileName")

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
        }
}