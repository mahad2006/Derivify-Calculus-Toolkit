package com.codewithmahad.derivativecalculator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codewithmahad.derivativecalculator.core.Step

@Composable
fun SolutionSteps(steps: List<Step>) {
    if (steps.isNotEmpty()) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Step-by-step solution:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                steps.forEach {
                    Text(it.rule, style = androidx.compose.material3.MaterialTheme.typography.titleSmall)
                    Text("Before: ${it.before}")
                    Text("After: ${it.after}")
                }
            }
        }
    }
}
