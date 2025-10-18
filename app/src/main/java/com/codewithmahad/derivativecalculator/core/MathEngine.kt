package com.codewithmahad.derivativecalculator.core

import com.codewithmahad.derivativecalculator.core.Expression

// Data class to hold each step of the differentiation process
data class Step(
    val rule: String,
    val before: String, // LaTeX representation
    val after: String   // LaTeX representation
)

// Data class to hold the final result and the steps
data class DerivativeResult(
    val finalExpression: Expression,
    val steps: List<Step>
)
