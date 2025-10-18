package com.codewithmahad.derivativecalculator.core

import java.lang.IllegalArgumentException

data class Variable(val name: Char) : Expression() {
    override fun toLatex(): String = name.toString()
    override fun evaluate(values: Map<Char, Double>): Double = values[name] ?: throw IllegalArgumentException("No value for '$name'")
    override fun getVariables(): Set<Char> = setOf(name)
}