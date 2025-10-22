package com.codewithmahad.derivativecalculator.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class HistoryItem(
    val function: String,
    val variable: Char,
    val order: Int,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Parcelize
data class Formula(
    val name: String,
    val category: String,
    val latex: String,
    val explanation: String,
    val exampleFunction: String,
    val exampleDerivative: String
) : Parcelable


data class MathSymbol(val displayText: String, val insertText: String = displayText)
enum class MathPanelTab(val title: String) {
    TRIG("Trig"),
    INVERSE("Inv"),    // "Inv" for Inverse
    HYPERBOLIC("Hyp"), // "Hyp" for Hyperbolic
    LOGS("Logs"),
    SYMBOLS("Syms")    // "Syms" for Symbols
}


data class FormulaItem(val latex: String, val description: String)

data class FormulaCategory(val title: String, val formulas: List<FormulaItem>)
