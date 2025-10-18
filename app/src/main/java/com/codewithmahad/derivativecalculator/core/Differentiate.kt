package com.codewithmahad.derivativecalculator.core



fun Expression.differentiate(variable: Char): DerivativeResult {
    val steps = mutableListOf<Step>()
    val finalExpression = this.differentiate(variable, steps)
    // Add a final simplification step
    val simplified = finalExpression.simplify()
    steps.add(Step("Final Simplification", finalExpression.toLatex(), simplified.toLatex()))
    return DerivativeResult(simplified, steps)
}

private fun Expression.differentiate(variable: Char, steps: MutableList<Step>): Expression {
    val result = when (this) {
        is Constant, is SymbolicConstant -> Constant(0.0)
        is Variable -> if (this.name == variable) Constant(1.0) else Constant(0.0)
        is Sum -> Sum(this.left.differentiate(variable, steps), this.right.differentiate(variable, steps))
        is Product -> {
            val u = this.left
            val v = this.right
            val du = u.differentiate(variable, steps)
            val dv = v.differentiate(variable, steps)
            val result = Sum(Product(du, v), Product(u, dv))
            steps.add(Step("Product Rule", this.toLatex(), result.toLatex()))
            result
        }
        is Quotient -> {
            val u = this.numerator
            val v = this.denominator
            val du = u.differentiate(variable, steps)
            val dv = v.differentiate(variable, steps)
            val num = Sum(Product(du, v), Product(Constant(-1.0), Product(u, dv)))
            val den = Power(v, Constant(2.0))
            val result = Quotient(num, den)
            steps.add(Step("Quotient Rule", this.toLatex(), result.toLatex()))
            result
        }
        is Power -> {
            // General Power Rule: d/dx(u^v) = u^v * (v' * ln(u) + v * u'/u)
            val u = this.base
            val v = this.exponent
            val du = u.differentiate(variable, steps)
            val dv = v.differentiate(variable, steps)

            if (v is Constant) { // Simple Power Rule: d/dx(u^n) = n*u^(n-1)*u'
                val n = v.value
                val newPower = Power(u, Constant(n - 1.0))
                val result = Product(Constant(n), Product(newPower, du))
                steps.add(Step("Power Rule", this.toLatex(), result.toLatex()))
                result
            } else if (u is Constant) { // Exponential Rule: d/dx(a^v) = a^v * ln(a) * v'
                val a = u.value
                val result = Product(this, Product(Ln(Constant(a)), dv))
                steps.add(Step("Exponential Rule", this.toLatex(), result.toLatex()))
                result
            } else { // General Power Rule
                val term1 = Product(dv, Ln(u))
                val term2 = Product(v, Quotient(du, u))
                val result = Product(this, Sum(term1, term2))
                steps.add(Step("General Power Rule", this.toLatex(), result.toLatex()))
                result
            }
        }
        is Sin -> {
            val du = this.argument.differentiate(variable, steps)
            val result = Product(Cos(this.argument), du)
            steps.add(Step("Chain Rule (sin)", this.toLatex(), result.toLatex()))
            result
        }
        is Cos -> {
            val du = this.argument.differentiate(variable, steps)
            val result = Product(Product(Constant(-1.0), Sin(this.argument)), du)
            steps.add(Step("Chain Rule (cos)", this.toLatex(), result.toLatex()))
            result
        }
        is Tan -> {
            val du = this.argument.differentiate(variable, steps)
            val secSquared = Power(Sec(this.argument), Constant(2.0))
            val result = Product(secSquared, du)
            steps.add(Step("Chain Rule (tan)", this.toLatex(), result.toLatex()))
            result
        }
        is Sec -> {
            val du = this.argument.differentiate(variable, steps)
            val result = Product(Product(Sec(this.argument), Tan(this.argument)), du)
            steps.add(Step("Chain Rule (sec)", this.toLatex(), result.toLatex()))
            result
        }
        is Csc -> {
            val du = this.argument.differentiate(variable, steps)
            val result = Product(Product(Constant(-1.0), Product(Csc(this.argument), Cot(this.argument))), du)
            steps.add(Step("Chain Rule (csc)", this.toLatex(), result.toLatex()))
            result
        }
        is Cot -> {
            val du = this.argument.differentiate(variable, steps)
            val cscSquared = Power(Csc(this.argument), Constant(2.0))
            val result = Product(Product(Constant(-1.0), cscSquared), du)
            steps.add(Step("Chain Rule (cot)", this.toLatex(), result.toLatex()))
            result
        }
        is Ln -> {
            val du = this.argument.differentiate(variable, steps)
            val result = Quotient(du, this.argument)
            steps.add(Step("Chain Rule (ln)", this.toLatex(), result.toLatex()))
            result
        }
        is Exp -> {
            val du = this.argument.differentiate(variable, steps)
            val result = Product(this, du)
            steps.add(Step("Chain Rule (exp)", this.toLatex(), result.toLatex()))
            result
        }
        is Asin -> {
            val u = this.argument
            val du = u.differentiate(variable, steps)
            val denominator = Power(Sum(Constant(1.0), Product(Constant(-1.0), Power(u, Constant(2.0)))), Constant(0.5))
            val result = Quotient(du, denominator)
            steps.add(Step("Chain Rule (arcsin)", this.toLatex(), result.toLatex()))
            result
        }
        is Acos -> {
            val u = this.argument
            val du = u.differentiate(variable, steps)
            val denominator = Power(Sum(Constant(1.0), Product(Constant(-1.0), Power(u, Constant(2.0)))), Constant(0.5))
            val result = Product(Constant(-1.0), Quotient(du, denominator))
            steps.add(Step("Chain Rule (arccos)", this.toLatex(), result.toLatex()))
            result
        }
        is Atan -> {
            val u = this.argument
            val du = u.differentiate(variable, steps)
            val denominator = Sum(Constant(1.0), Power(u, Constant(2.0)))
            val result = Quotient(du, denominator)
            steps.add(Step("Chain Rule (arctan)", this.toLatex(), result.toLatex()))
            result
        }
        is Asec -> {
            val u = this.argument
            val du = u.differentiate(variable, steps)
            val denominator = Product(Abs(u), Power(Sum(Power(u, Constant(2.0)), Constant(-1.0)), Constant(0.5)))
            val result = Quotient(du, denominator)
            steps.add(Step("Chain Rule (arcsec)", this.toLatex(), result.toLatex()))
            result
        }
        is Acsc -> {
            val u = this.argument
            val du = u.differentiate(variable, steps)
            val denominator = Product(Abs(u), Power(Sum(Power(u, Constant(2.0)), Constant(-1.0)), Constant(0.5)))
            val result = Product(Constant(-1.0), Quotient(du, denominator))
            steps.add(Step("Chain Rule (arccsc)", this.toLatex(), result.toLatex()))
            result
        }
        is Acot -> {
            val u = this.argument
            val du = u.differentiate(variable, steps)
            val denominator = Sum(Constant(1.0), Power(u, Constant(2.0)))
            val result = Product(Constant(-1.0), Quotient(du, denominator))
            steps.add(Step("Chain Rule (arccot)", this.toLatex(), result.toLatex()))
            result
        }
        is Sinh -> {
            val du = this.argument.differentiate(variable, steps)
            val result = Product(Cosh(this.argument), du)
            steps.add(Step("Chain Rule (sinh)", this.toLatex(), result.toLatex()))
            result
        }
        is Cosh -> {
            val du = this.argument.differentiate(variable, steps)
            val result = Product(Sinh(this.argument), du)
            steps.add(Step("Chain Rule (cosh)", this.toLatex(), result.toLatex()))
            result
        }
        is Tanh -> {
            val du = this.argument.differentiate(variable, steps)
            val sechSquared = Power(Sech(this.argument), Constant(2.0))
            val result = Product(sechSquared, du)
            steps.add(Step("Chain Rule (tanh)", this.toLatex(), result.toLatex()))
            result
        }
        is Csch -> {
            val du = this.argument.differentiate(variable, steps)
            val result = Product(Product(Constant(-1.0), Product(Csch(this.argument), Coth(this.argument))), du)
            steps.add(Step("Chain Rule (csch)", this.toLatex(), result.toLatex()))
            result
        }
        is Sech -> {
            val du = this.argument.differentiate(variable, steps)
            val result = Product(Product(Constant(-1.0), Product(Sech(this.argument), Tanh(this.argument))), du)
            steps.add(Step("Chain Rule (sech)", this.toLatex(), result.toLatex()))
            result
        }
        is Coth -> {
            val du = this.argument.differentiate(variable, steps)
            val cschSquared = Power(Csch(this.argument), Constant(2.0))
            val result = Product(Product(Constant(-1.0), cschSquared), du)
            steps.add(Step("Chain Rule (coth)", this.toLatex(), result.toLatex()))
            result
        }
        is Abs -> {
            val du = this.argument.differentiate(variable, steps)
            val result = Product(Quotient(this.argument, this), du)
            steps.add(Step("Chain Rule (abs)", this.toLatex(), result.toLatex()))
            result
        }
        else -> Constant(0.0)
    }
    return result
}
