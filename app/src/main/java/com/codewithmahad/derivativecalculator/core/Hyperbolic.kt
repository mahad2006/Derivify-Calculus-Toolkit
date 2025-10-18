package com.codewithmahad.derivativecalculator.core

import com.codewithmahad.derivativecalculator.core.Expression
import kotlin.math.sinh
import kotlin.math.cosh
import kotlin.math.tanh

data class Sinh(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Sinh(argument.simplify())
    override fun toLatex(): String = "\\sinh(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = sinh(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}

data class Cosh(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Cosh(argument.simplify())
    override fun toLatex(): String = "\\cosh(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = cosh(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}

data class Tanh(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Tanh(argument.simplify())
    override fun toLatex(): String = "\\tanh(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = tanh(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}

data class Sech(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Sech(argument.simplify())
    override fun toLatex(): String = "\\text{sech}(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = 1.0 / cosh(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}

data class Csch(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Csch(argument.simplify())
    override fun toLatex(): String = "\\text{csch}(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = 1.0 / sinh(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}

data class Coth(val argument: Expression) : Expression() {
    override fun simplify(): Expression = Coth(argument.simplify())
    override fun toLatex(): String = "\\text{coth}(${argument.toLatex()})"
    override fun evaluate(values: Map<Char, Double>): Double = 1.0 / tanh(argument.evaluate(values))
    override fun getVariables(): Set<Char> = argument.getVariables()
}