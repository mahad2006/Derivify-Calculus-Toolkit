package com.codewithmahad.derivativecalculator.core

import java.lang.IllegalArgumentException

data class Quotient(val numerator: Expression, val denominator: Expression) : Expression() {
    override fun simplify(): Expression {
        val sNum = numerator.simplify(); val sDenom = denominator.simplify()
        if (sNum is Constant && sNum.value == 0.0) return Constant(0.0)
        if (sDenom is Constant && sDenom.value == 1.0) return sNum
        return Quotient(sNum, sDenom)
    }
    override fun toLatex(): String = "\\frac{${numerator.toLatex()}}{${denominator.toLatex()}}"
    override fun evaluate(values: Map<Char, Double>): Double {
        val denValue = denominator.evaluate(values)
        if (denValue == 0.0) {
            throw IllegalArgumentException("Division by zero.")
        }
        return numerator.evaluate(values) / denValue
    }
    override fun getVariables(): Set<Char> = numerator.getVariables() + denominator.getVariables()
}