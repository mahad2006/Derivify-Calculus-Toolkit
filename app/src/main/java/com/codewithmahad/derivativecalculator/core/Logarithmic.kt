package com.codewithmahad.derivativecalculator.core

import com.codewithmahad.derivativecalculator.core.Expression
import kotlin.math.ln
import kotlin.math.exp

data class Ln(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Ln(argument.simplify())
    override fun toLatex(): String = "\\ln(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double {
        val argValue = argument.evaluate(values)
        if (argValue <= 0) {
            throw IllegalArgumentException("Logarithm of a non-positive number.")
        }
        return ln(argValue)
    }
    override fun getVariables(): Set<Char> = argument.getVariables()
}

data class Exp(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Exp(argument.simplify())
    override fun toLatex(): String = "e^{${argument.toLatex()}}"
    override fun evaluate(values: Map<Char, Double>): Double = exp(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}