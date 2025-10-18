package com.codewithmahad.derivativecalculator.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.codewithmahad.derivativecalculator.core.Step
import com.codewithmahad.derivativecalculator.core.differentiate
import com.codewithmahad.derivativecalculator.core.parse
import java.util.Locale
import kotlin.math.abs

// This class inherits from BaseCalculatorViewModel to get the shared keyboard logic.
class LinearApproximationViewModel(application: Application) : BaseCalculatorViewModel(application) {

    // --- STATE FOR LINEAR APPROXIMATION ---
    var linearVariablesInput by mutableStateOf("")
    var linearPointInput by mutableStateOf("")
    var linearResultText by mutableStateOf("")
    var rawLinearLatexResult by mutableStateOf("")
    var linearFunctionError by mutableStateOf<String?>(null)
    var calculationSteps by mutableStateOf<List<Step>>(emptyList())

    // --- EVENT HANDLERS FOR THIS SPECIFIC SCREEN ---
    // Update the function input without performing validation here
    fun onLinearFunctionChange(newValue: TextFieldValue) {
        functionInput = newValue
        // Clear the error when the user types
        if (linearFunctionError != null) {
            linearFunctionError = null
        }
    }

    fun onLinearVariablesChange(newValue: String) {
        if (newValue.all { it.isLetter() || it == ',' || it.isWhitespace() }) {
            linearVariablesInput = newValue
        }
    }

    fun onLinearPointChange(newValue: String) {
        if (newValue.all { it.isDigit() || it == ',' || it.isWhitespace() || it == '.' || it == '-' }) {
            linearPointInput = newValue
        }
    }

    fun onLinearClearClicked() {
        functionInput = TextFieldValue("")
        linearVariablesInput = ""
        linearPointInput = ""
        linearResultText = ""
        rawLinearLatexResult = ""
        linearFunctionError = null
        calculationSteps = emptyList()
    }

    // --- THE CORE LOGIC FOR LINEAR APPROXIMATION ---
    fun onLinearCalculateClicked() {
        // Step 1: Validate input. Only check for blankness here.
        if (functionInput.text.isBlank()) {
            linearFunctionError = "Function cannot be empty"
            linearResultText = "Please enter a function to continue."
            return
        } else {
            // Clear any previous errors before attempting calculation
            linearFunctionError = null
        }

        try {
            val variables = linearVariablesInput.split(',').map { it.trim().first() }
            val pointValues = linearPointInput.split(',').map { it.trim().toDouble() }
            if (variables.size != pointValues.size) {
                throw IllegalArgumentException("Number of variables must match number of point values.")
            }

            val functionStr = functionInput.text
            val pointMap = variables.zip(pointValues).toMap()

            // The 'parse' function will throw an exception if the format is invalid
            val functionExpression = parse(functionStr)
            val f_at_point = functionExpression.evaluate(pointMap)

            val resultTerms = mutableListOf<String>()
            resultTerms.add(String.format(Locale.US, "%.4f", f_at_point))

            val allSteps = mutableListOf<Step>()

            variables.forEachIndexed { index, variable ->
                val derivativeResult = functionExpression.differentiate(variable)
                val partialDerivative = derivativeResult.finalExpression
                allSteps.addAll(derivativeResult.steps)
                val fx_at_point = partialDerivative.evaluate(pointMap)
                val a = pointValues[index]

                if (fx_at_point != 0.0) {
                    val termSign = if (fx_at_point > 0) " + " else " - "
                    val coeff = String.format(Locale.US, "%.4f", abs(fx_at_point))
                    val pointSign = if (a > 0) "-" else "+"
                    val pointVal = abs(a)

                    resultTerms.add("$termSign$coeff( $variable $pointSign $pointVal )")
                }
            }

            calculationSteps = allSteps

            val resultEquation = resultTerms.joinToString("")

            rawLinearLatexResult = resultEquation
            val variablesFormatted = variables.joinToString(",")
            linearResultText = "L($variablesFormatted) = $resultEquation"

        } catch (e: Exception) {
            // If any error occurs during parsing or calculation, display it.
            linearFunctionError = e.message ?: "Invalid expression"
            linearResultText = "Error: ${e.message ?: "Invalid expression"}"
            e.printStackTrace()
        }
    }
}