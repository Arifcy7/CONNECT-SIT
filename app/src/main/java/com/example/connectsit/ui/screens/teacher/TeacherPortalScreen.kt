package com.example.connectsit.ui.screens.teacher


import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Menu

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherPortalScreen() {
    val Bluish = Color(0xFF523EC8)
    val i = 0
    val ModuleCounter = 1
    val Modules = listOf("Module 1", "Module 2", "Module 3", "Module 4", "Module 5")
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Bluish,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text(
                        "CREATE MODULES",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            tint = Color.White,
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"

                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        (innerPadding)
    }



}









