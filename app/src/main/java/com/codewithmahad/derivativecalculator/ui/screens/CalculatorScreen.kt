package com.codewithmahad.derivativecalculator.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codewithmahad.derivativecalculator.viewmodels.SettingsViewModel
import com.codewithmahad.derivativecalculator.ui.components.EvaluationCard
import com.codewithmahad.derivativecalculator.ui.components.LatexView
import com.codewithmahad.derivativecalculator.ui.components.OrderStepper
import com.codewithmahad.derivativecalculator.viewmodels.PartialDerivativeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CalculatorScreen(
    viewModel: PartialDerivativeViewModel,
    onMathInputFocused: (Boolean) -> Unit,
    contentPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onShowSteps: () -> Unit
) {
    val variableFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val haptics = LocalHapticFeedback.current
    val settingsViewModel: SettingsViewModel = viewModel()
    val hapticsEnabled by settingsViewModel.hapticFeedbackEnabled.collectAsState()
    val precision by settingsViewModel.precision.collectAsState()
    viewModel.precision = precision
    val clipboardManager = LocalClipboardManager.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = viewModel.functionInput,
                        onValueChange = { viewModel.onFunctionChange(it) },
                        label = { Text("Function f(...)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState -> onMathInputFocused(focusState.isFocused) },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { variableFocusRequester.requestFocus() }),
                        isError = viewModel.functionError != null,
                        supportingText = { if (viewModel.functionError != null) Text(text = viewModel.functionError!!) }
                    )
                    OutlinedTextField(
                        value = viewModel.variableInput,
                        onValueChange = { viewModel.onVariableChange(it) },
                        label = { Text("Variable") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(variableFocusRequester)
                            .onFocusChanged { focusState -> onMathInputFocused(false) }, // Always hide math panel for this field
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }),
                        isError = viewModel.variableError != null,
                        supportingText = { if (viewModel.variableError != null) Text(text = viewModel.variableError!!) }
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    OrderStepper(
                        order = viewModel.derivativeOrder,
                        onDecrease = { viewModel.onOrderDecrease() },
                        onIncrease = { viewModel.onOrderIncrease() }
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.onClearClicked() },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) { Text("Clear All") }
                        Button(
                            onClick = {
                                if (hapticsEnabled) {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                }
                                viewModel.onCalculateClicked()

                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Filled.Calculate, null)
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("Calculate")
                        }
                    }
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = viewModel.resultText.isNotBlank(),
                enter = slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = spring(
                        dampingRatio = 0.6f,
                        stiffness = 200f
                    )
                ) + fadeIn(animationSpec = spring()),
                exit = slideOutVertically() + fadeOut()
            ) {
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
                                Text("Result", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                                if (viewModel.calculationSteps.isNotEmpty()) {
                                    IconButton(onClick = onShowSteps) {
                                        Icon(Icons.Default.ArrowForward, "Show steps")
                                    }
                                }
                                IconButton(onClick = {
                                    clipboardManager.setText(AnnotatedString(viewModel.rawLatexResult))
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Equation copied to clipboard",
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    if (hapticsEnabled) { haptics.performHapticFeedback(HapticFeedbackType.LongPress) }
                                }) {
                                    Icon(Icons.Default.ContentCopy, "Copy Equation")
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LatexView(latex = viewModel.resultText)
                        }
                    }

                    if (viewModel.evaluationPoints.isNotEmpty()) {
                        EvaluationCard(
                            points = viewModel.evaluationPoints,
                            numericalResult = viewModel.numericalResult,
                            onPointValueChanged = { v, s -> viewModel.onPointValueChanged(v, s) },
                            onEvaluateClicked = { viewModel.onEvaluateClicked() }
                        )
                    }
                }
            }
        }
    }
}