package com.codewithmahad.derivativecalculator.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import com.codewithmahad.derivativecalculator.core.validateExpression

// This now extends AndroidViewModel to get access to the Application context.
open class BaseCalculatorViewModel(application: Application) : AndroidViewModel(application) {

    // This state is generic and will be used by child ViewModels
    var functionInput by mutableStateOf(TextFieldValue(""))
    var isExpressionValid by mutableStateOf(true)
        private set

    protected fun validate(input: TextFieldValue) {
        isExpressionValid = if (input.text.isEmpty()) {
            true // Or false, depending on whether you consider empty to be valid
        } else {
            validateExpression(input.text)
        }
        functionInput = input
    }

    // This function is shared by all calculators
    fun onMathSymbolClicked(symbol: String) {
        val currentText = functionInput.text
        val selection = functionInput.selection
        val newText = currentText.replaceRange(selection.start, selection.end, symbol)
        val newCursorPos = if (symbol.endsWith("()")) {
            selection.start + symbol.length - 1
        } else {
            selection.start + symbol.length
        }
        validate(
            TextFieldValue(
                text = newText,
                selection = TextRange(newCursorPos)
            )
        )
    }

    fun onBackspace() {
        val currentText = functionInput.text
        val selection = functionInput.selection
        if (currentText.isNotEmpty() && selection.start > 0) {
            val newText = currentText.removeRange(selection.start - 1, selection.start)
            validate(
                TextFieldValue(
                    text = newText,
                    selection = TextRange(selection.start - 1)
                )
            )
        }
    }

    fun onMoveCursor(offset: Int) {
        val selection = functionInput.selection
        val newOffset = (selection.start + offset).coerceIn(0, functionInput.text.length)
        validate(functionInput.copy(selection = TextRange(newOffset)))
    }
}