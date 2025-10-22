package com.codewithmahad.derivativecalculator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.codewithmahad.derivativecalculator.viewmodels.GradientViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun GradientScreen(
    viewModel: GradientViewModel,
    onMathInputFocused: (Boolean) -> Unit,
    contentPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val variablesFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    CalculatorScreenLayout(
        pageTitle = "Gradient Formula",
        formulaLatex = "\\nabla f = \\frac{\\partial f}{\\partial x} \\mathbf{i} + \\frac{\\partial f}{\\partial y} \\mathbf{j} + \\dots",
        contentPadding = contentPadding,
        resultText = viewModel.gradientResultText,
        rawLatexToCopy = viewModel.rawGradientLatexResult,
        snackbarHostState = snackbarHostState,
        scope = scope,
        inputContent = {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.functionInput,
                    onValueChange = { viewModel.onGradientFunctionChange(it) },
                    label = { Text("Function f(x, y, ...)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->

                            onMathInputFocused(focusState.isFocused)
                        },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    isError = !viewModel.isExpressionValid || viewModel.gradientFunctionError != null,
                    supportingText = { if (viewModel.gradientFunctionError != null) Text(text = viewModel.gradientFunctionError!!) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { variablesFocusRequester.requestFocus() })
                )

                OutlinedTextField(
                    value = viewModel.gradientVariablesInput,
                    onValueChange = { viewModel.onGradientVariablesChange(it) },
                    label = { Text("Variables (e.g., x,y,z)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(variablesFocusRequester)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                onMathInputFocused(false)
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    isError = viewModel.gradientVariablesError != null,
                    supportingText = { if (viewModel.gradientVariablesError != null) Text(text = viewModel.gradientVariablesError!!) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.onGradientClearClicked() },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Clear All") }
                    Button(
                        onClick = { viewModel.onGradientCalculateClicked() },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Calculate ∇f") }
                }
            }
        }
    )
}