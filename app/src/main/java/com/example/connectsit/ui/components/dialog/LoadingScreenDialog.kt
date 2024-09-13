
package com.alexafit.fitjournal.core.presentation.commoncomponents.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingScreenDialog(
    onBackPress: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {
            onBackPress()
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(32.dp)
            )
            .border(
                width = 2.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(32.dp)
            ),
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = true
        ),
        content = {
            Box(
                modifier = Modifier.background(color = Color.Transparent),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(75.dp),
                    color = Color.Blue
                )
            }
        }
    )
}
