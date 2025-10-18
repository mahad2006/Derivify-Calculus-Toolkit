package com.codewithmahad.derivativecalculator.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.codewithmahad.derivativecalculator.SettingsRepository
import com.codewithmahad.derivativecalculator.core.DerivativeResult
import com.codewithmahad.derivativecalculator.core.Expression
import com.codewithmahad.derivativecalculator.core.Step
import com.codewithmahad.derivativecalculator.core.differentiate
import com.codewithmahad.derivativecalculator.core.parse
import com.codewithmahad.derivativecalculator.data.HistoryItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class PartialDerivativeViewModel(application: Application) : BaseCalculatorViewModel(application) {

    private val repository = SettingsRepository(application)

    var variableInput by mutableStateOf("")
    var resultText by mutableStateOf("")
    var rawLatexResult by mutableStateOf("")
    var functionError by mutableStateOf<String?>(null)
    var variableError by mutableStateOf<String?>(null)
    var derivativeOrder by mutableStateOf(1)
    var evaluationPoints by mutableStateOf<Map<Char, String>>(emptyMap())
    var numericalResult by mutableStateOf<String?>(null)
    var calculationHistory by mutableStateOf<List<HistoryItem>>(emptyList())
    var calculationSteps by mutableStateOf<List<Step>>(emptyList())
    var functionDataPoints by mutableStateOf<List<Pair<Float, Float>>>(emptyList())
    var derivativeDataPoints by mutableStateOf<List<Pair<Float, Float>>>(emptyList())
    var precision by mutableStateOf(4)
    private var originalExpression: Expression? = null
    private var lastDerivativeExpression: Expression? = null
    private var _lastDeletedItem: HistoryItem? = null

    init {
        // Load the history from DataStore when the ViewModel is created
        viewModelScope.launch {
            calculationHistory = repository.historyFlow.first()
        }
    }

    fun onFunctionChange(newValue: TextFieldValue) {
        functionInput = newValue
        if (functionError != null) {
            functionError = null
        }
    }

    fun onVariableChange(input: String) {
        if (input.isEmpty() || (input.length <= 1 && input.all { it.isLetter() })) {
            variableInput = input
            variableError = if (input.isBlank()) "Variable cannot be empty" else null
        } else {
            variableError = "Must be a single letter"
        }
    }

    fun onOrderIncrease() { if (derivativeOrder < 10) derivativeOrder++ }
    fun onOrderDecrease() { if (derivativeOrder > 1) derivativeOrder-- }

    fun onClearClicked() {
        functionInput = TextFieldValue("")
        variableInput = ""
        resultText = ""
        derivativeOrder = 1
        evaluationPoints = emptyMap()
        numericalResult = null
        lastDerivativeExpression = null
        originalExpression = null
        functionDataPoints = emptyList()
        derivativeDataPoints = emptyList()
        calculationSteps = emptyList()
    }

    fun onCalculateClicked() {
        if (functionInput.text.isBlank()) {
            functionError = "Function cannot be empty"
            resultText = "Please enter a function to continue."
            return
        }

        onVariableChange(variableInput)
        if (variableError != null || variableInput.isBlank()) {
            resultText = "Please fix the errors above."
            return
        }
        val diffVar = variableInput.first()
        numericalResult = null
        evaluationPoints = emptyMap()

        try {
            var currentExpression = parse(functionInput.text)
            originalExpression = currentExpression
            val allSteps = mutableListOf<Step>()
            for (i in 1..derivativeOrder) {
                val derivativeResult = currentExpression.differentiate(diffVar)
                currentExpression = derivativeResult.finalExpression
                allSteps.addAll(derivativeResult.steps)
            }
            lastDerivativeExpression = currentExpression

            val finalResultLatex = lastDerivativeExpression!!.toLatex()
            rawLatexResult = finalResultLatex
            val notation = if (derivativeOrder > 1) {
                "\\frac{\\partial^{$derivativeOrder} f}{\\partial $diffVar^{$derivativeOrder}}"
            } else {
                "\\frac{\\partial f}{\\partial $diffVar}"
            }
            resultText = "$notation = $finalResultLatex"
            calculationSteps = allSteps

            val historyItem = HistoryItem(functionInput.text, diffVar, derivativeOrder, resultText)
            val updatedHistory = (listOf(historyItem) + calculationHistory).take(50) // Keep the last 50
            calculationHistory = updatedHistory
            viewModelScope.launch { repository.saveHistory(updatedHistory) }

            val variablesInResult = lastDerivativeExpression!!.getVariables()
            if (variablesInResult.isNotEmpty()) {
                evaluationPoints = variablesInResult.associateWith { "" }
            }
            generateGraphData()
        } catch (e: Exception) {
            functionError = e.message ?: "Invalid expression"
            resultText = "Error: ${e.message ?: "Invalid expression"}"
            e.printStackTrace()
        }
    }

    fun onPointValueChanged(variable: Char, value: String) {
        evaluationPoints = evaluationPoints.toMutableMap().apply { put(variable, value) }
    }

    fun onEvaluateClicked() {
        lastDerivativeExpression?.let { evaluateExpression(it) }
    }

    private fun evaluateExpression(expression: Expression) {
        try {
            val valuesAsDoubles = evaluationPoints.mapValues {
                it.value.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid number for ${it.key}")
            }
            val evalResult = expression.evaluate(valuesAsDoubles)
            val pattern = "#." + "#".repeat(precision)
            val df = DecimalFormat(pattern)
            numericalResult = if (evalResult.rem(1).equals(0.0)) evalResult.toLong().toString() else df.format(evalResult)
        } catch (e: Exception) {
            numericalResult = "Error"
        }
    }

    fun loadFromHistory(item: HistoryItem) {
        functionInput = TextFieldValue(item.function)
        variableInput = item.variable.toString()
        derivativeOrder = item.order
        resultText = item.result
        evaluationPoints = emptyMap()
        numericalResult = null
        lastDerivativeExpression = null
        originalExpression = null
        functionDataPoints = emptyList()
        derivativeDataPoints = emptyList()
        calculationSteps = emptyList()
    }

    fun deleteHistoryItem(item: HistoryItem) {
        _lastDeletedItem = item
        val updatedHistory = calculationHistory.filterNot { it.timestamp == item.timestamp }
        calculationHistory = updatedHistory
        viewModelScope.launch { repository.saveHistory(updatedHistory) }
    }

    fun undoDelete() {
        _lastDeletedItem?.let {
            val updatedHistory = (listOf(it) + calculationHistory)
                .sortedByDescending { item -> item.timestamp }
            calculationHistory = updatedHistory
            viewModelScope.launch { repository.saveHistory(updatedHistory) }
            _lastDeletedItem = null
        }
    }

    fun clearAllHistory() {
        calculationHistory = emptyList()
        viewModelScope.launch { repository.saveHistory(emptyList()) }
    }

    private fun generateGraphData() {
        val originalFunc = originalExpression ?: return
        val derivativeFunc = lastDerivativeExpression ?: return
        val plotVar = variableInput.firstOrNull() ?: return

        val functionPoints = mutableListOf<Pair<Float, Float>>()
        val derivativePoints = mutableListOf<Pair<Float, Float>>()

        for (i in -100..100) {
            val x = i / 10f
            try {
                val yFunc = originalFunc.evaluate(mapOf(plotVar to x.toDouble())).toFloat()
                val yDeriv = derivativeFunc.evaluate(mapOf(plotVar to x.toDouble())).toFloat()
                if (yFunc.isFinite()) functionPoints.add(x to yFunc)
                if (yDeriv.isFinite()) derivativePoints.add(x to yDeriv)
            } catch (e: Exception) { /* Ignore undefined points */ }
        }
        functionDataPoints = functionPoints
        derivativeDataPoints = derivativePoints
    }
}