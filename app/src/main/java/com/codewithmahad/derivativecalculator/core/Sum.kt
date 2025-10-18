package com.codewithmahad.derivativecalculator.core

import com.codewithmahad.derivativecalculator.core.Constant
import com.codewithmahad.derivativecalculator.core.Product

data class Sum(val left: Expression, val right: Expression) : Expression() {
    override fun simplify(): Expression {
        fun collectTerms(exp: Expression, list: MutableList<Expression>) {
            if (exp is Sum) {
                collectTerms(exp.left, list)
                collectTerms(exp.right, list)
            } else {
                list.add(exp.simplify())
            }
        }

        fun extractCoefficientAndTerm(exp: Expression): Pair<Double, Expression> {
            return when {
                exp is Product && exp.left is Constant -> Pair(exp.left.value, exp.right)
                exp is Constant -> Pair(exp.value, Constant(1.0))
                else -> Pair(1.0, exp)
            }
        }

        val terms = mutableListOf<Expression>()
        collectTerms(this, terms)

        val coefficients = mutableMapOf<Expression, Double>()
        terms.forEach { term ->
            val (coeff, baseTerm) = extractCoefficientAndTerm(term)
            coefficients[baseTerm] = (coefficients[baseTerm] ?: 0.0) + coeff
        }

        val finalTerms = mutableListOf<Expression>()
        coefficients.forEach { (term, coeff) ->
            if (coeff != 0.0) {
                val newTerm = when {
                    term == Constant(1.0) -> Constant(coeff)
                    coeff == 1.0 -> term
                    coeff == -1.0 -> Product(Constant(-1.0), term)
                    else -> Product(Constant(coeff), term)
                }
                finalTerms.add(newTerm.simplify())
            }
        }

        if (finalTerms.isEmpty()) return Constant(0.0)

        finalTerms.sortBy { it.getSortOrder() }

        var result: Expression = finalTerms.first()
        for (i in 1 until finalTerms.size) {
            result = Sum(result, finalTerms[i])
        }
        return result
    }

    override fun toLatex(): String {
        val rightLatex = right.toLatex()
        if (rightLatex.startsWith("-")) {
            return "${left.toLatex()} - ${rightLatex.substring(1)}"
        }
        return "${left.toLatex()} + $rightLatex"
    }

    override fun evaluate(values: Map<Char, Double>): Double = left.evaluate(values) + right.evaluate(values)
    override fun getVariables(): Set<Char> = left.getVariables() + right.getVariables()
}