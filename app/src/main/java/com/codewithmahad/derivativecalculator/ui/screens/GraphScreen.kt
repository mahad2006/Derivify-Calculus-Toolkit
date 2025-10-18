package com.codewithmahad.derivativecalculator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.codewithmahad.derivativecalculator.R
import com.codewithmahad.derivativecalculator.ui.components.CustomMarkerView
import com.codewithmahad.derivativecalculator.viewmodels.PartialDerivativeViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun GraphScreen(
    viewModel: PartialDerivativeViewModel,
    contentPadding: PaddingValues
) {
    if (viewModel.functionDataPoints.isEmpty() && viewModel.derivativeDataPoints.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoGraph,
                    contentDescription = "No Graph Data",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Text(
                    text = "No graph to display",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "Calculate a function on the main screen to see its graph here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding) // Apply padding
                .padding(16.dp)
        ) {
            Text("Function Graph", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            LineChartComposable(
                functionData = viewModel.functionDataPoints,
                derivativeData = viewModel.derivativeDataPoints
            )
        }
    }
}

@Composable
fun LineChartComposable(
    functionData: List<Pair<Float, Float>>,
    derivativeData: List<Pair<Float, Float>>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val functionColor = MaterialTheme.colorScheme.primary
    val derivativeColor = MaterialTheme.colorScheme.secondary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    AndroidView(
        factory = { 
            val chart = LineChart(context)
            val marker = CustomMarkerView(context, R.layout.custom_marker_view)
            chart.marker = marker
            chart
        },
        modifier = modifier.fillMaxSize(),
        update = { chart ->
            val funcEntries = functionData.map { Entry(it.first, it.second) }
            val derivEntries = derivativeData.map { Entry(it.first, it.second) }

            val funcDataSet = LineDataSet(funcEntries, "f(x)").apply {
                color = functionColor.toArgb()
                setDrawCircles(false)
                lineWidth = 2.5f
                highLightColor = functionColor.copy(alpha = 0.5f).toArgb()
            }

            val derivDataSet = LineDataSet(derivEntries, "f'(x)").apply {
                color = derivativeColor.toArgb()
                setDrawCircles(false)
                lineWidth = 2f
                enableDashedLine(10f, 8f, 0f)
                highLightColor = derivativeColor.copy(alpha = 0.5f).toArgb()
            }

            chart.data = LineData(funcDataSet, derivDataSet)

            chart.setTouchEnabled(true)
            chart.isDragEnabled = true
            chart.setScaleEnabled(true)
            chart.setPinchZoom(true)

            chart.legend.textColor = onSurfaceColor.toArgb()
            chart.legend.textSize = 12f

            chart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = onSurfaceColor.toArgb()
                gridColor = onSurfaceColor.copy(alpha = 0.1f).toArgb()
            }
            chart.axisLeft.apply {
                textColor = onSurfaceColor.toArgb()
                gridColor = onSurfaceColor.copy(alpha = 0.1f).toArgb()
            }
            chart.axisRight.isEnabled = false

            chart.isHighlightPerTapEnabled = true
            chart.isHighlightPerDragEnabled = true
            
            chart.description.isEnabled = false
            chart.invalidate()
        })
}