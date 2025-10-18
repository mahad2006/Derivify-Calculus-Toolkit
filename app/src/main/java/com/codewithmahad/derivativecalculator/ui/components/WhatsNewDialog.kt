package com.codewithmahad.derivativecalculator.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun WhatsNewDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("What's New in Version 1.0.0") },
        text = {
            Text(
                "- Added 'Smart' History Clearing: Automatically clear your calculation history after a set period.\n"
                + "- Added 'What\'s New' Dialog: See the latest updates and features right here!\n"
                + "- Minor bug fixes and performance improvements."
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}
