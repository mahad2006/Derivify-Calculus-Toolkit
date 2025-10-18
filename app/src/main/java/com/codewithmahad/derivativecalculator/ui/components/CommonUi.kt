package com.codewithmahad.derivativecalculator.ui.components

import android.webkit.WebView
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.codewithmahad.derivativecalculator.data.FormulaItem

@Composable
fun LatexView(latex: String, fontSize: TextUnit = 18.sp) {
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant
    val webTextColor = String.format("#%06X", (0xFFFFFF and textColor.toArgb()))
    val fontSizeValue = fontSize.value

    val htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/katex@0.16.9/dist/katex.min.css">
                <script src="https://cdn.jsdelivr.net/npm/katex@0.16.9/dist/katex.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/katex@0.16.9/dist/contrib/auto-render.min.js"></script>
                <style>
                    body {
                        background-color: transparent;
                        color: $webTextColor;
                        font-size: ${fontSizeValue}sp;
                        text-align: left;
                        margin: 0;
                        padding: 8px;
                        overflow-wrap: break-word;
                    }
                </style>
            </head>
            <body>
                $$${latex}$$
                <script>
                    renderMathInElement(document.body, { delimiters: [ {left: '$$', right: '$$', display: true} ] });
                </script>
            </body>
            </html>
        """
        .trimIndent()

    val horizontalScrollState = rememberScrollState()

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .horizontalScroll(horizontalScrollState),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                setBackgroundColor(Color.Transparent.toArgb())
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        }
    )
}

@Composable
fun OrderStepper(
    order: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier // Add this
) {
    Row(
        modifier = modifier, // And apply it here
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Order:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp)
        )
        OutlinedIconButton(onClick = onDecrease, enabled = order > 1) {
            Icon(Icons.Default.Remove, contentDescription = "Decrease Order")
        }
        Text(text = "$order", style = MaterialTheme.typography.headlineSmall)
        OutlinedIconButton(onClick = onIncrease, enabled = order < 10) {
            Icon(Icons.Default.Add, contentDescription = "Increase Order")
        }
    }
}

@Composable
fun EvaluationCard(
    points: Map<Char, String>,
    numericalResult: String?,
    onPointValueChanged: (Char, String) -> Unit,
    onEvaluateClicked: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Evaluate at a Point", style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            points.keys.sorted().forEach { variable ->
                OutlinedTextField(
                    value = points[variable] ?: "",
                    onValueChange = { onPointValueChanged(variable, it) },
                    label = { Text("Value for $variable") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onEvaluateClicked,
                modifier = Modifier.align(Alignment.End),
                enabled = points.values.all { it.isNotBlank() }
            ) {
                Text("Evaluate")
            }

            if (numericalResult != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Numerical Result = $numericalResult",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
// You can keep SolutionStepsCard and LatexStepView if you plan to re-add them later.

@Composable
fun SolutionStepsCard(steps: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Solution Steps",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Display each step using a row with a number and the LatexStepView
            steps.forEachIndexed { index, step ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}.",
                        modifier = Modifier.padding(end = 8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    LatexStepView(latex = step)
                }
                if (index < steps.lastIndex) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
fun LatexStepView(latex: String) {
    val textColor = MaterialTheme.colorScheme.onSurface
    val webTextColor = String.format("#%06X", (0xFFFFFF and textColor.toArgb()))

    val htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/katex@0.16.9/dist/katex.min.css">
                <script src="https://cdn.jsdelivr.net/npm/katex@0.16.9/dist/katex.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/katex@0.16.9/dist/contrib/auto-render.min.js"></script>
                <style>
                    body {
                        background-color: transparent;
                        color: $webTextColor;
                        font-size: 1.1em;
                        text-align: left;
                        margin: 0; padding: 0;
                        line-height: 1.5;
                    }
                </style>
            </head>
            <body>
                ${latex}
                <script>
                    renderMathInElement(document.body, {
                        delimiters: [
                            {left: "$$", right: "$$", display: true},
                            {left: "$", right: "$", display: false}
                        ]
                    });
                </script>
            </body>
            </html>
        """
        .trimIndent()

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                setBackgroundColor(Color.Transparent.toArgb())
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        }
    )
}
@Composable
fun FormulaCard(item: FormulaItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            LatexView(latex = item.latex)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun InProgressScreen(featureName: String, contentPadding: PaddingValues) { // <-- ADD THE PARAMETER
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding) // <-- APPLY THE PADDING
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Construction, // The university/school icon is perfect
            contentDescription = "Coming Soon",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "$featureName Calculator",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        // --- THE NEW, RELATABLE MESSAGE FOR KARACHI UNI STUDENTS ---
        Text(
            text = "Believe me, this feature is on the list! It's currently queued behind a mountain of assignments, and a final exam that's worth way too much of my GPA.\n\nI'll get to it right after I survive this semester. Send good vibes (and maybe some chai)! ☕",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoTooltip(tooltipText: String) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                Text(tooltipText)
            }
        },
        state = rememberTooltipState()
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "Info",
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
