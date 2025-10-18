package com.codewithmahad.derivativecalculator.core

fun validateExpression(expression: String): Boolean {
    return try {
        parse(expression)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}

fun parse(input: String): Expression {
    val sanitized = input.replace(" ", "").replace("+-", "-").replace("--", "+").replace("(-", "(0-")
    if(sanitized.isEmpty()) throw IllegalArgumentException("Function cannot be empty")
    return parseSum(sanitized)
}

private fun parseSum(input: String): Expression {
    var pCount = 0
    for (i in input.length - 1 downTo 0) {
        when (input[i]) {
            ')' -> pCount++
            '(' -> pCount--
        }
        if (pCount == 0) {
            when (input[i]) {
                '+' -> return Sum(parse(input.substring(0, i)), parse(input.substring(i + 1)))
                '-' -> if (i > 0) return Sum(parse(input.substring(0, i)), Product(Constant(-1.0), parse(input.substring(i + 1))))
            }
        }
    }
    return parseProduct(input)
}

private fun parseProduct(input: String): Expression {
    var pCount = 0
    for (i in input.length - 1 downTo 0) {
        when (input[i]) {
            ')' -> pCount++
            '(' -> pCount--
        }
        if (pCount == 0) {
            when (input[i]) {
                '*' -> return Product(parse(input.substring(0, i)), parse(input.substring(i + 1)))
                '/' -> return Quotient(parse(input.substring(0, i)), parse(input.substring(i + 1)))
            }
        }
    }
    return parseParenthesesProduct(input)
}

private fun parseParenthesesProduct(input: String): Expression {
    var pCount = 0
    for (i in 1 until input.length) {
        when (input[i]) {
            '(' -> pCount++
            ')' -> pCount--
        }
        if (pCount == 0 && input[i] == '(' && input[i - 1] == ')') {
            return Product(parse(input.substring(0, i)), parse(input.substring(i)))
        }
    }
    return parsePower(input)
}

private fun parsePower(input: String): Expression {
    input.lastIndexOf('^').takeIf { it != -1 }?.let { i ->
        var pCount = 0
        val prefix = input.substring(0, i)
        for(char in prefix) {
            if (char == '(') pCount++
            if (char == ')') pCount--
        }
        if (pCount == 0) return Power(parse(input.substring(0, i)), parse(input.substring(i + 1)))
    }
    return parseFactor(input)
}

// In CalculatorViewModel.kt

private fun parseFactor(input: String): Expression {
    if (input.startsWith("-")) return Product(Constant(-1.0), parse(input.substring(1)))
    if (input == "π") return SymbolicConstant('π')
    if (input == "e") return SymbolicConstant('e')

    fun extractArgument(functionName: String, expression: String): String {
        val content = expression.substring(functionName.length + 1, expression.length - 1)
        if (content.isEmpty()) { throw IllegalArgumentException("Argument for $functionName() cannot be empty.") }
        return content
    }

    if (input.startsWith("sqrt(") && input.endsWith(")")) return Power(parse(extractArgument("sqrt", input)), Constant(0.5))
    if (input.startsWith("sin(") && input.endsWith(")")) return Sin(parse(extractArgument("sin", input)))
    if (input.startsWith("cos(") && input.endsWith(")")) return Cos(parse(extractArgument("cos", input)))
    if (input.startsWith("tan(") && input.endsWith(")")) return Tan(parse(extractArgument("tan", input)))
    if (input.startsWith("sec(") && input.endsWith(")")) return Sec(parse(extractArgument("sec", input)))
    if (input.startsWith("csc(") && input.endsWith(")")) return Csc(parse(extractArgument("csc", input)))
    if (input.startsWith("cot(") && input.endsWith(")")) return Cot(parse(extractArgument("cot", input)))
    if (input.startsWith("ln(") && input.endsWith(")")) return Ln(parse(extractArgument("ln", input)))
    if (input.startsWith("exp(") && input.endsWith(")")) return Exp(parse(extractArgument("exp", input)))
    if (input.startsWith("asin(") && input.endsWith(")")) return Asin(parse(extractArgument("asin", input)))
    if (input.startsWith("acos(") && input.endsWith(")")) return Acos(parse(extractArgument("acos", input)))
    if (input.startsWith("atan(") && input.endsWith(")")) return Atan(parse(extractArgument("atan", input)))
    if (input.startsWith("asec(") && input.endsWith(")")) return Asec(parse(extractArgument("asec", input)))
    if (input.startsWith("acsc(") && input.endsWith(")")) return Acsc(parse(extractArgument("acsc", input)))
    if (input.startsWith("acot(") && input.endsWith(")")) return Acot(parse(extractArgument("acot", input)))
    if (input.startsWith("sinh(") && input.endsWith(")")) return Sinh(parse(extractArgument("sinh", input)))
    if (input.startsWith("cosh(") && input.endsWith(")")) return Cosh(parse(extractArgument("cosh", input)))
    if (input.startsWith("tanh(") && input.endsWith(")")) return Tanh(parse(extractArgument("tanh", input)))
    if (input.startsWith("sech(") && input.endsWith(")")) return Sech(parse(extractArgument("sech", input)))
    if (input.startsWith("csch(") && input.endsWith(")")) return Csch(parse(extractArgument("csch", input)))
    if (input.startsWith("coth(") && input.endsWith(")")) return Coth(parse(extractArgument("coth", input)))

    if (input.startsWith("(") && input.endsWith(")")) return parse(input.substring(1, input.length - 1))
    input.toDoubleOrNull()?.let { return Constant(it) }
    if (input.length == 1 && input[0].isLetter()) return Variable(input[0])
    throw IllegalArgumentException("Invalid expression format: $input")
}
