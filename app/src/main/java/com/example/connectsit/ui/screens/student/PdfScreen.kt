package com.example.connectsit.ui.screens.student

import android.content.Context
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
import androidx.navigation.NavController
import com.example.connectsit.utils.downloadPdf
import com.example.connectsit.utils.fetchPdfList
import com.example.connectsit.utils.getCategory
import com.example.connectsit.utils.getCourseName
import com.example.connectsit.utils.openPdfViewer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
        try {
            // Fetch PDFs and map to the correct type
            val fetchedPdfList = fetchPdfList(courseName, category)
            pdfList = fetchedPdfList.map { mapToStudentPdfFile(it) }
            if (pdfList.isEmpty()) {
                errorMessage = "Nothing found for this course and category."
            }
        } catch (e: Exception) {
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
                title = { Text("LIST") },
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
                    "Nothing available for this course and category.",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
                else -> LazyColumn {
                    items(pdfList) { pdf ->
                        PdfListItem(pdf = pdf, context = context)
                    }
                }
            }
        }
    }
}

@Composable
fun PdfListItem(pdf: PdfFile, context: Context) {
    val coroutineScope = rememberCoroutineScope()
    var isDownloading by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                coroutineScope.launch {
                    isDownloading = true
                    openPdfViewer(context, pdf.downloadUrl, pdf.name)
                    isDownloading = false
                }
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Filled.PictureAsPdf, contentDescription = "PDF Icon", tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = pdf.name, color = Color.White)
            if (isDownloading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}


fun mapToStudentPdfFile(pdfFile: com.example.connectsit.utils.PdfFile): PdfFile {
    return PdfFile(
        name = pdfFile.name,
        downloadUrl = pdfFile.downloadUrl
    )
}
