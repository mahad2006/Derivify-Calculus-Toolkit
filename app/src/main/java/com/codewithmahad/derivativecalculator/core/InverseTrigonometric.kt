package com.codewithmahad.derivativecalculator.core

import com.codewithmahad.derivativecalculator.core.Expression
import kotlin.math.asin
import kotlin.math.acos
import kotlin.math.atan

data class Asin(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Asin(argument.simplify())
    override fun toLatex(): String = "\\arcsin(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = asin(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}
data class Acos(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Acos(argument.simplify())
    override fun toLatex(): String = "\\arccos(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = acos(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}
data class Atan(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Atan(argument.simplify())
    override fun toLatex(): String = "\\arctan(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = atan(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}
data class Asec(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Asec(argument.simplify())
    override fun toLatex(): String = "\\operatorname{arcsec}(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = acos(1.0 / argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}
data class Acsc(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Acsc(argument.simplify())
    override fun toLatex(): String = "\\operatorname{arccsc}(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = asin(1.0 / argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}
data class Acot(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Acot(argument.simplify())
    override fun toLatex(): String = "\\operatorname{arccot}(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = atan(1.0 / argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}