package com.codewithmahad.derivativecalculator.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.codewithmahad.derivativecalculator.core.differentiate
import com.codewithmahad.derivativecalculator.core.parse

class GradientViewModel(application: Application) : BaseCalculatorViewModel(application) {
    var gradientVariablesInput by mutableStateOf("")
    var gradientResultText by mutableStateOf("")
    var rawGradientLatexResult by mutableStateOf("")
    var gradientFunctionError by mutableStateOf<String?>(null)
    var gradientVariablesError by mutableStateOf<String?>(null)

    fun onGradientFunctionChange(newValue: TextFieldValue) {
        validate(newValue)
        gradientFunctionError = if (newValue.text.isBlank()) "Function cannot be empty" else null
    }

    fun onGradientVariablesChange(newValue: String) {
        if (newValue.all { it.isLetter() || it == ',' || it.isWhitespace() }) {
            gradientVariablesInput = newValue
            gradientVariablesError = if (newValue.isBlank()) "Variables cannot be empty" else null
        }
    }

    fun onGradientClearClicked() {
        functionInput = TextFieldValue("")
        gradientVariablesInput = ""
        gradientResultText = ""
        rawGradientLatexResult = ""
        gradientFunctionError = null
        gradientVariablesError = null
    }

    fun onGradientCalculateClicked() {
        onGradientFunctionChange(functionInput)
        onGradientVariablesChange(gradientVariablesInput)

        if (gradientFunctionError != null || gradientVariablesError != null ||
            functionInput.text.isBlank() || gradientVariablesInput.isBlank()
        ) {
            gradientResultText = "Please fix the errors above."
            return
        }

        val variables = gradientVariablesInput.split(',').map { it.trim() }.filter { it.isNotEmpty() }
        val functionStr = functionInput.text
        val vectorComponents = listOf("i", "j", "k")

        try {
            val partialResults = mutableListOf<String>()
            for (variable in variables) {
                if (variable.length != 1) {
                    throw IllegalArgumentException("'$variable' is not a valid single character variable.")
                }
                val diffVar = variable.first()
                val partialDerivative = parse(functionStr).differentiate(diffVar).finalExpression
                partialResults.add("(${partialDerivative.toLatex()})")
            }

            val resultString = partialResults.mapIndexed { index, result ->
                if (index < vectorComponents.size) {
                    "$result \\mathbf{${vectorComponents[index]}}"
                } else {
                    "$result \\mathbf{e}_{${index + 1}}"
                }
            }.joinToString(" + ")

            gradientResultText = "\\nabla f = $resultString"
            rawGradientLatexResult = resultString

        } catch (e: Exception) {
            gradientResultText = "Error: ${e.message}"
            e.printStackTrace()
        }
    }
}