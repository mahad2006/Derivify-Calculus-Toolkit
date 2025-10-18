package com.codewithmahad.derivativecalculator.core


data class Constant(val value: Double) : Expression() {
    override fun toLatex(): String = if (value.rem(1).equals(0.0)) value.toLong().toString() else value.toString()
    override fun evaluate(values: Map<Char, Double>): Double = value
    override fun getVariables(): Set<Char> = emptySet()
}