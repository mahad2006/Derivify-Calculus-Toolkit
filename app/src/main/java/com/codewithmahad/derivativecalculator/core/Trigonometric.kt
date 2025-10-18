package com.codewithmahad.derivativecalculator.core

import com.codewithmahad.derivativecalculator.core.Expression
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.tan

data class Sin(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Sin(argument.simplify())
    override fun toLatex(): String = "\\sin(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = sin(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}
data class Cos(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Cos(argument.simplify())
    override fun toLatex(): String = "\\cos(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = cos(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}
data class Tan(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Tan(argument.simplify())
    override fun toLatex(): String = "\\tan(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = tan(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}
data class Sec(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Sec(argument.simplify())
    override fun toLatex(): String = "\\sec(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = 1.0 / cos(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}
data class Csc(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Csc(argument.simplify())
    override fun toLatex(): String = "\\csc(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = 1.0 / sin(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}
data class Cot(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Cot(argument.simplify())
    override fun toLatex(): String = "\\cot(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = 1.0 / tan(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}