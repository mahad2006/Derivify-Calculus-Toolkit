package com.codewithmahad.derivativecalculator.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.codewithmahad.derivativecalculator.ui.components.LatexView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CalculatorScreenLayout(
    pageTitle: String,
    formulaLatex: String,
    contentPadding: PaddingValues,
    resultText: String,
    rawLatexToCopy: String,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    inputContent: @Composable () -> Unit,
    additionalContent: @Composable () -> Unit = {}
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() + 16.dp,
            bottom = contentPadding.calculateBottomPadding() + 16.dp,
            start = 16.dp,
            end = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // --- Item 1: The Formula Card (Now generic) ---
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(pageTitle, style = MaterialTheme.typography.titleMedium)
                    LatexView(latex = formulaLatex)
                }
            }
        }

        // --- Item 2: The Input Card (Uses the slot) ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                // The specific TextFields and Buttons go here
                inputContent()
            }
        }

        // --- Item 3: The Result Card (Now generic) ---
        item {
            AnimatedVisibility(visible = resultText.isNotBlank()) {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Result",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = {
                                    clipboardManager.setText(AnnotatedString(rawLatexToCopy))
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Equation copied to clipboard",
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy Equation"
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LatexView(latex = resultText)
                        }
                    }
                    additionalContent()
                }
            }
        }
    }
}