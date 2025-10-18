package com.codewithmahad.derivativecalculator.core

import kotlin.math.E
import kotlin.math.PI

data class SymbolicConstant(val symbol: Char) : Expression() {
    // The derivative of a constant is always 0.
    override fun evaluate(values: Map<Char, Double>): Double = when (symbol) {
        'π' -> PI
        'e' -> E
        else -> Double.NaN // Should not happen
    }
    // They are constants, not variables.
    override fun getVariables(): Set<Char> = emptySet()
    // Convert the symbol to its LaTeX representation.
    override fun toLatex(): String = when (symbol) {
        'π' -> "pi"
        'e' -> "e"
        else -> symbol.toString()
    }
}