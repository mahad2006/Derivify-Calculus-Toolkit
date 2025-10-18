package com.codewithmahad.derivativecalculator.core

import com.codewithmahad.derivativecalculator.core.Constant
import com.codewithmahad.derivativecalculator.core.Power
import com.codewithmahad.derivativecalculator.core.Sum
import com.codewithmahad.derivativecalculator.core.Quotient
import com.codewithmahad.derivativecalculator.core.Variable
import com.codewithmahad.derivativecalculator.core.SymbolicConstant

data class Product(val left: Expression, val right: Expression) : Expression() {
    /**
     * THIS IS THE FULLY REWRITTEN SIMPLIFY FUNCTION
     */
    override fun simplify(): Expression {
        // Helper to flatten the product tree into a list of terms
        fun collectTerms(exp: Expression, list: MutableList<Expression>) {
            if (exp is Product) {
                collectTerms(exp.left, list)
                collectTerms(exp.right, list)
            } else {
                list.add(exp)
            }
        }

        // 1. Flatten the entire product chain and simplify each term first
        val terms = mutableListOf<Expression>()
        collectTerms(this, terms)
        val simplifiedTerms = terms.map { it.simplify() }

        // 2. Process terms: separate constants, signs, and other expressions
        val constants = mutableListOf<Double>()
        val otherTerms = mutableListOf<Expression>()
        var negativeCount = 0

        simplifiedTerms.forEach { term ->
            when {
                term is Product && term.left is Constant && term.left.value == -1.0 -> {
                    negativeCount++
                    otherTerms.add(term.right)
                }
                term is Constant -> {
                    if (term.value < 0) {
                        negativeCount++
                        if (term.value != -1.0) constants.add(-term.value)
                    } else if (term.value != 1.0) {
                        constants.add(term.value)
                    }
                }
                else -> otherTerms.add(term)
            }
        }

        // Handle multiplication by zero
        if (constants.contains(0.0)) return Constant(0.0)

        // 3. *** NEW LOGIC: Group identical terms into powers ***
        val termCounts = otherTerms.groupingBy { it }.eachCount()
        val groupedTerms = termCounts.map { (term, count) ->
            if (count > 1) {
                // Create a Power expression, e.g., Power(x, 2)
                Power(term, Constant(count.toDouble())).simplify()
            } else {
                term
            }
        }.toMutableList()

        // 4. Sort the now-grouped terms by their type
        groupedTerms.sortBy { it.getSortOrder() }

        // 5. Rebuild the expression tree
        val combinedConstant = constants.fold(1.0) { acc, d -> acc * d }
        val finalTerms = mutableListOf<Expression>()
        if (combinedConstant != 1.0) {
            finalTerms.add(Constant(combinedConstant))
        }
        finalTerms.addAll(groupedTerms)

        val isNegative = negativeCount % 2 != 0

        if (finalTerms.isEmpty()) {
            return if (isNegative) Constant(-1.0) else Constant(1.0)
        }

        var finalExpression: Expression = finalTerms.first()
        for (i in 1 until finalTerms.size) {
            finalExpression = Product(finalExpression, finalTerms[i])
        }

        if (isNegative) {
            return Product(Constant(-1.0), finalExpression)
        }

        return finalExpression
    }
    override fun toLatex(): String {
        // Handle multiplication by -1, which is a common case.
        if (left is Constant && left.value == -1.0) {
            return when (right) {
                is Sum, is Product, is Quotient -> "-(${right.toLatex()})"
                else -> "-${right.toLatex()}"
            }
        }

        // Special formatting for the derivative of a square root.
        if (left is Constant && left.value == 0.5 && right is Power && right.exponent is Constant && right.exponent.value == -0.5) {
            return "\\frac{1}{2\\sqrt{${right.base.toLatex()}}}"
        }
        // Same pattern, but swapped.
        if (right is Constant && right.value == 0.5 && left is Power && left.exponent is Constant && left.exponent.value == -0.5) {
            return "\\frac{1}{2\\sqrt{${left.base.toLatex()}}}"
        }

        val leftTex = when (left) {
            is Sum, is Quotient -> "(${left.toLatex()})"
            else -> left.toLatex()
        }
        val rightTex = when (right) {
            is Sum, is Quotient -> "(${right.toLatex()})"
            else -> right.toLatex()
        }

        // Determine if an explicit multiplication dot is needed.
        // Implicit multiplication is used for simple cases like 2x, xy, or 2sin(x).
        fun isSimple(e: Expression) = e is Variable || e is SymbolicConstant

        val separator = when {
            // Constant coefficient: 2x, 2*pi, 2*x^2, 2*sin(x)
            left is Constant && right !is Constant -> ""
            // Juxtaposition of variables/symbols: xy, x*pi
            isSimple(left) && isSimple(right) -> ""
            // Variable next to power: x*y^2
            isSimple(left) && right is Power -> ""
            // Power next to variable: x^2*y
            left is Power && isSimple(right) -> ""
            // Everything else gets a dot
            else -> " \\cdot "
        }

        return "$leftTex$separator$rightTex"
    }
    override fun evaluate(values: Map<Char, Double>): Double = left.evaluate(values) * right.evaluate(values)
    override fun getVariables(): Set<Char> = left.getVariables() + right.getVariables()
}
