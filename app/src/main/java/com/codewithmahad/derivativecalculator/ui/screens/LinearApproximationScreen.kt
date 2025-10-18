package com.codewithmahad.derivativecalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.codewithmahad.derivativecalculator.ui.components.LatexView
import com.codewithmahad.derivativecalculator.viewmodels.LinearApproximationViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun LinearApproximationScreen(
    viewModel: LinearApproximationViewModel,
    onMathInputFocused: (Boolean) -> Unit,
    contentPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val variablesFocusRequester = remember { FocusRequester() }
    val pointFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    CalculatorScreenLayout(
        pageTitle = "Linear Approximation Formula",
        formulaLatex = "L(x,y) = f(a,b) + f_x(a,b)(x-a) + f_y(a,b)(y-b)",
        contentPadding = contentPadding,
        resultText = viewModel.linearResultText,
        rawLatexToCopy = viewModel.rawLinearLatexResult,
        snackbarHostState = snackbarHostState,
        scope = scope,
        inputContent = {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.functionInput, // Uses the value from the base ViewModel
                    onValueChange = { viewModel.onLinearFunctionChange(it) },
                    label = { Text("Function f(x, y, ...)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            onMathInputFocused(focusState.isFocused)
                        },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    isError = viewModel.linearFunctionError != null,
                    supportingText = { if (viewModel.linearFunctionError != null) Text(text = viewModel.linearFunctionError!!) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { variablesFocusRequester.requestFocus() })
                )
                OutlinedTextField(
                    value = viewModel.linearVariablesInput,
                    onValueChange = { viewModel.onLinearVariablesChange(it) },
                    label = { Text("Variables (e.g., x,y)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(variablesFocusRequester)
                        .onFocusChanged { focusState -> if (focusState.isFocused) onMathInputFocused(false) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { pointFocusRequester.requestFocus() })
                )
                OutlinedTextField(
                    value = viewModel.linearPointInput,
                    onValueChange = { viewModel.onLinearPointChange(it) },
                    label = { Text("Point (e.g., 1,2)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(pointFocusRequester)
                        .onFocusChanged { focusState -> if (focusState.isFocused) onMathInputFocused(false) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.onLinearClearClicked() },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Clear All") }
                    Button(
                        onClick = { viewModel.onLinearCalculateClicked() },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Calculate") }
                }
            }
        },
        additionalContent = {
            if (viewModel.calculationSteps.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Step-by-step Solution",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyColumn(modifier = Modifier.height(300.dp)) { // Constrain height
                            items(viewModel.calculationSteps.size) { index ->
                                val step = viewModel.calculationSteps[index]
                                Text(step.rule, style = MaterialTheme.typography.titleSmall)
                                LatexView(latex = "\\frac{d}{dx}[${step.before}] = ${step.after}")
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    )
}