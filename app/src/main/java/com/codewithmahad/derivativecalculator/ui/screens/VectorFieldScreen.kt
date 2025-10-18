package com.codewithmahad.derivativecalculator.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codewithmahad.derivativecalculator.viewmodels.VectorFieldData
import com.codewithmahad.derivativecalculator.viewmodels.VectorFieldMode
import com.codewithmahad.derivativecalculator.viewmodels.VectorFieldViewModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun VectorFieldScreen(
    contentPadding: PaddingValues,
    viewModel: VectorFieldViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp)
    ) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = viewModel.pComponentInput,
                        onValueChange = { viewModel.onPComponentChange(it) },
                        label = { Text("P(x, y)") },
                        modifier = Modifier.weight(1f),
                        isError = viewModel.pComponentError != null,
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = viewModel.qComponentInput,
                        onValueChange = { viewModel.onQComponentChange(it) },
                        label = { Text("Q(x, y)") },
                        modifier = Modifier.weight(1f),
                        isError = viewModel.qComponentError != null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { viewModel.onVisualizeClicked() })
                    )
                }
                if (viewModel.pComponentError != null || viewModel.qComponentError != null) {
                    Text(
                        text = viewModel.pComponentError ?: viewModel.qComponentError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.onVisualizeClicked() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Visualize")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        VectorFieldVisualization(
            vectorFieldData = viewModel.vectorFieldData,
            mode = viewModel.currentMode,
            onModeChange = { viewModel.currentMode = it }
        )
    }
}

@Composable
fun VectorFieldVisualization(
    vectorFieldData: VectorFieldData?,
    mode: VectorFieldMode,
    onModeChange: (VectorFieldMode) -> Unit
) {
    if (vectorFieldData == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Grain, "Vector Field Icon", modifier = Modifier.height(100.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Enter a vector field and press \"Visualize\".")
            }
        }
    } else {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = mode == VectorFieldMode.FIELD, onClick = { onModeChange(VectorFieldMode.FIELD) })
                Text("Field")
                Spacer(Modifier.width(16.dp))
                RadioButton(selected = mode == VectorFieldMode.DIVERGENCE, onClick = { onModeChange(VectorFieldMode.DIVERGENCE) })
                Text("Divergence")
                Spacer(Modifier.width(16.dp))
                RadioButton(selected = mode == VectorFieldMode.CURL, onClick = { onModeChange(VectorFieldMode.CURL) })
                Text("Curl")
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) { drawVectorField(vectorFieldData, mode) }
            }
        }
    }
}

private fun DrawScope.drawVectorField(data: VectorFieldData, mode: VectorFieldMode) {
    val gridSize = 21 // -10 to 10
    val step = size.width / gridSize
    val arrowLength = step * 0.4f

    val maxDivergence = data.divergence.maxOfOrNull { it.let(::abs) } ?: 1f
    val maxCurl = data.curl.maxOfOrNull { it.let(::abs) } ?: 1f

    data.gridPoints.forEachIndexed { index, (x, y) ->
        val canvasX = (x + 10) * step + step / 2
        val canvasY = size.height - ((y + 10) * step + step / 2)

        when (mode) {
            VectorFieldMode.FIELD -> {
                val (vx, vy) = data.vectors[index]
                val magnitude = sqrt(vx * vx + vy * vy)
                if (magnitude > 1e-6) {
                    val normVx = vx / magnitude
                    val normVy = vy / magnitude
                    drawArrow(
                        color = Color.Blue,
                        start = Offset(canvasX, canvasY),
                        end = Offset(canvasX + normVx * arrowLength, canvasY - normVy * arrowLength)
                    )
                }
            }
            VectorFieldMode.DIVERGENCE -> {
                val divergence = data.divergence[index]
                val color = when {
                    divergence > 0 -> Color.Red.copy(alpha = min(1f, divergence / maxDivergence))
                    else -> Color.Blue.copy(alpha = min(1f, -divergence / maxDivergence))
                }
                drawCircle(color, radius = step / 3, center = Offset(canvasX, canvasY))
            }
            VectorFieldMode.CURL -> {
                val curl = data.curl[index]
                val color = when {
                    curl > 0 -> Color.Green.copy(alpha = min(1f, curl / maxCurl))
                    else -> Color.Magenta.copy(alpha = min(1f, -curl / maxCurl))
                }
                drawCircle(color, radius = step / 3, center = Offset(canvasX, canvasY), style = Stroke(width = 4f))
            }
        }
    }
}

private fun DrawScope.drawArrow(color: Color, start: Offset, end: Offset, strokeWidth: Float = 4f) {
    drawLine(color, start, end, strokeWidth)
    val angle = atan2((end.y - start.y), (end.x - start.x))
    val arrowHeadLength = strokeWidth * 3
    drawLine(
        color = color,
        start = end,
        end = Offset((end.x - arrowHeadLength * cos(angle - Math.PI / 6)).toFloat(), (end.y - arrowHeadLength * sin(angle - Math.PI / 6)).toFloat()),
        strokeWidth = strokeWidth
    )
    drawLine(
        color = color,
        start = end,
        end = Offset((end.x - arrowHeadLength * cos(angle + Math.PI / 6)).toFloat(), (end.y - arrowHeadLength * sin(angle + Math.PI / 6)).toFloat()),
        strokeWidth = strokeWidth
    )
}

private fun abs(f: Float): Float = if (f < 0) -f else f
