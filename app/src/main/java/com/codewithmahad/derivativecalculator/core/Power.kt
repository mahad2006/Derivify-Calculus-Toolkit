package com.codewithmahad.derivativecalculator.core

import kotlin.math.pow

data class Power(val base: Expression, val exponent: Expression) : Expression() {
    override fun simplify(): Expression {
        val sBase = base.simplify()
        val sExp = exponent.simplify()

        // --- FIX 1: Simplify a power of a power, e.g., (x^2)^3 = x^6 ---
        if (sBase is Power && sBase.exponent is Constant && sExp is Constant) {
            val newExponent = sBase.exponent.value * sExp.value
            // Return a new Power expression and simplify it again, in case the new exponent is 1 or 0
            return Power(sBase.base, Constant(newExponent)).simplify()
        }

        // Existing simplification logic
        if (sExp is Constant && sExp.value == 0.0) return Constant(1.0)
        if (sExp is Constant && sExp.value == 1.0) return sBase
        return Power(sBase, sExp)
    }

    override fun toLatex(): String {
        // --- FIX 2: Display any power of 0.5 as a square root ---
        if (exponent is Constant && exponent.value == 0.5) {
            return "\\sqrt{${base.toLatex()}}"
        }

        // Existing toLatex logic
        val baseTex =
            if (base is Sum || base is Product || base is Quotient || base is Power) "(${base.toLatex()})" else base.toLatex()
        return "$baseTex^{${exponent.toLatex()}}"
    }
    override fun evaluate(values: Map<Char, Double>): Double = base.evaluate(values).pow(exponent.evaluate(values))
    override fun getVariables(): Set<Char> = base.getVariables() + exponent.getVariables()
}