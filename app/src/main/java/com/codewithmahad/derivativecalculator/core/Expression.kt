package com.codewithmahad.derivativecalculator.core

import kotlin.math.abs

sealed class Expression {
    open fun simplify(): Expression = this
    abstract fun toLatex(): String
    abstract fun evaluate(values: Map<Char, Double>): Double
    abstract fun getVariables(): Set<Char>

    fun getSortOrder(): Int = when (this) {
        is Constant, is SymbolicConstant -> 1
        is Variable -> 2
        is Power -> 3
        else -> 4 // All other functions
    }
}

data class Abs(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Abs(argument.simplify())
    override fun toLatex(): String = "|${argument.toLatex()}|"
    override fun evaluate(values: Map<Char, Double>): Double = abs(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}