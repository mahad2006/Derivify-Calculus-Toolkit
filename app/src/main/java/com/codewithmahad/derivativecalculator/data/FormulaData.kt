package com.codewithmahad.derivativecalculator.data

object FormulaData {
    fun getAllFormulas(): List<Formula> {
        return listOf(
            // Basic Rules
            Formula(
                name = "Power Rule",
                category = "Basic Rules",
                latex = "\\frac{d}{dx}(x^n) = nx^{n-1}",
                explanation = "The power rule is used to differentiate functions of the form f(x) = x^n, where n is any real number. To find the derivative, you bring the exponent down as a coefficient and then subtract one from the original exponent.",
                exampleFunction = "f(x) = x^3",
                exampleDerivative = "f'(x) = 3x^2"
            ),
            Formula(
                name = "Product Rule",
                category = "Basic Rules",
                latex = "\\frac{d}{dx}(uv) = u\\frac{dv}{dx} + v\\frac{du}{dx}",
                explanation = "The product rule is used to find the derivative of a product of two functions. The derivative is the first function times the derivative of the second, plus the second function times the derivative of the first.",
                exampleFunction = "f(x) = x^2 \\sin(x)",
                exampleDerivative = "f'(x) = x^2 \\cos(x) + 2x \\sin(x)"
            ),
            Formula(
                name = "Quotient Rule",
                category = "Basic Rules",
                latex = "\\frac{d}{dx}(\\frac{u}{v}) = \\frac{v\\frac{du}{dx} - u\\frac{dv}{dx}}{v^2}",
                explanation = "The quotient rule is used for finding the derivative of a quotient of two functions. It's often remembered by the mnemonic 'low d-high minus high d-low, square the bottom and away we go'.",
                exampleFunction = "f(x) = \\frac{\\sin(x)}{x}",
                exampleDerivative = "f'(x) = \\frac{x\\cos(x) - \\sin(x)}{x^2}"
            ),
            Formula(
                name = "Chain Rule",
                category = "Basic Rules",
                latex = "\\frac{d}{dx}(f(g(x))) = f'(g(x))g'(x)",
                explanation = "The chain rule is used to differentiate composite functions. It states that the derivative of a composite function is the derivative of the outer function evaluated at the inner function, multiplied by the derivative of the inner function.",
                exampleFunction = "f(x) = (x^2 + 1)^3",
                exampleDerivative = "f'(x) = 3(x^2+1)^2 \\cdot 2x = 6x(x^2+1)^2"
            ),

            // Trigonometric
            Formula(
                name = "Sine",
                category = "Trigonometric Functions",
                latex = "\\frac{d}{dx}(\\sin(x)) = \\cos(x)",
                explanation = "The derivative of the sine function is the cosine function.",
                exampleFunction = "f(x) = \\sin(x)",
                exampleDerivative = "f'(x) = \\cos(x)"
            ),
            Formula(
                name = "Cosine",
                category = "Trigonometric Functions",
                latex = "\\frac{d}{dx}(\\cos(x)) = -\\sin(x)",
                explanation = "The derivative of the cosine function is the negative sine function.",
                exampleFunction = "f(x) = \\cos(x)",
                exampleDerivative = "f'(x) = -\\sin(x)"
            ),
            Formula(
                name = "Tangent",
                category = "Trigonometric Functions",
                latex = "\\frac{d}{dx}(\\tan(x)) = \\sec^2(x)",
                explanation = "The derivative of the tangent function is the secant squared function.",
                exampleFunction = "f(x) = \\tan(x)",
                exampleDerivative = "f'(x) = \\sec^2(x)"
            ),
            Formula(
                name = "Secant",
                category = "Trigonometric Functions",
                latex = "\\frac{d}{dx}(\\sec(x)) = \\sec(x)\\tan(x)",
                explanation = "The derivative of the secant function is secant times tangent.",
                exampleFunction = "f(x) = \\sec(x)",
                exampleDerivative = "f'(x) = \\sec(x)\\tan(x)"
            ),
            Formula(
                name = "Cosecant",
                category = "Trigonometric Functions",
                latex = "\\frac{d}{dx}(\\csc(x)) = -\\csc(x)\\cot(x)",
                explanation = "The derivative of the cosecant function is negative cosecant times cotangent.",
                exampleFunction = "f(x) = \\csc(x)",
                exampleDerivative = "f'(x) = -\\csc(x)\\cot(x)"
            ),
            Formula(
                name = "Cotangent",
                category = "Trigonometric Functions",
                latex = "\\frac{d}{dx}(\\cot(x)) = -\\csc^2(x)",
                explanation = "The derivative of the cotangent function is negative cosecant squared.",
                exampleFunction = "f(x) = \\cot(x)",
                exampleDerivative = "f'(x) = -\\csc^2(x)"
            ),

            // Exponential & Logarithmic
            Formula(
                name = "Natural Logarithm",
                category = "Exponential & Logarithmic",
                latex = "\\frac{d}{dx}(\\ln(x)) = \\frac{1}{x}",
                explanation = "The derivative of the natural logarithm of x is 1 divided by x.",
                exampleFunction = "f(x) = \\ln(x)",
                exampleDerivative = "f'(x) = \\frac{1}{x}"
            ),
            Formula(
                name = "Exponential Function (e^x)",
                category = "Exponential & Logarithmic",
                latex = "\\frac{d}{dx}(e^x) = e^x",
                explanation = "The exponential function e^x is its own derivative.",
                exampleFunction = "f(x) = e^x",
                exampleDerivative = "f'(x) = e^x"
            ),
            Formula(
                name = "Exponential Function (a^x)",
                category = "Exponential & Logarithmic",
                latex = "\\frac{d}{dx}(a^x) = a^x \\ln(a)",
                explanation = "The derivative of an exponential function with a base 'a' is the function itself multiplied by the natural log of the base.",
                exampleFunction = "f(x) = 2^x",
                exampleDerivative = "f'(x) = 2^x \\ln(2)"
            ),

            // Inverse Trigonometric
            Formula(
                name = "Arcsine",
                category = "Inverse Trigonometric",
                latex = "\\frac{d}{dx}(\\arcsin(x)) = \\frac{1}{\\sqrt{1-x^2}}",
                explanation = "The derivative of arcsin(x) is 1 over the square root of (1 - x^2).",
                exampleFunction = "f(x) = \\arcsin(x)",
                exampleDerivative = "f'(x) = \\frac{1}{\\sqrt{1-x^2}}"
            ),
            Formula(
                name = "Arccosine",
                category = "Inverse Trigonometric",
                latex = "\\frac{d}{dx}(\\arccos(x)) = -\\frac{1}{\\sqrt{1-x^2}}",
                explanation = "The derivative of arccos(x) is -1 over the square root of (1 - x^2).",
                exampleFunction = "f(x) = \\arccos(x)",
                exampleDerivative = "f'(x) = -\\frac{1}{\\sqrt{1-x^2}}"
            ),
            Formula(
                name = "Arctangent",
                category = "Inverse Trigonometric",
                latex = "\\frac{d}{dx}(\\arctan(x)) = \\frac{1}{1+x^2}",
                explanation = "The derivative of arctan(x) is 1 over (1 + x^2).",
                exampleFunction = "f(x) = \\arctan(x)",
                exampleDerivative = "f'(x) = \\frac{1}{1+x^2}"
            ),
            Formula(
                name = "Arcsecant",
                category = "Inverse Trigonometric",
                latex = "\\frac{d}{dx}(\\operatorname{arcsec}(x)) = \\frac{1}{|x|\\sqrt{x^2-1}}",
                explanation = "The derivative of arcsec(x) is 1 over the absolute value of x times the square root of (x^2 - 1).",
                exampleFunction = "f(x) = \\operatorname{arcsec}(x)",
                exampleDerivative = "f'(x) = \\frac{1}{|x|\\sqrt{x^2-1}}"
            ),
            Formula(
                name = "Arccosecant",
                category = "Inverse Trigonometric",
                latex = "\\frac{d}{dx}(\\operatorname{arccsc}(x)) = -\\frac{1}{|x|\\sqrt{x^2-1}}",
                explanation = "The derivative of arccsc(x) is -1 over the absolute value of x times the square root of (x^2 - 1).",
                exampleFunction = "f(x) = \\operatorname{arccsc}(x)",
                exampleDerivative = "f'(x) = -\\frac{1}{|x|\\sqrt{x^2-1}}"
            ),
            Formula(
                name = "Arccotangent",
                category = "Inverse Trigonometric",
                latex = "\\frac{d}{dx}(\\operatorname{arccot}(x)) = -\\frac{1}{1+x^2}",
                explanation = "The derivative of arccot(x) is -1 over (1 + x^2).",
                exampleFunction = "f(x) = \\operatorname{arccot}(x)",
                exampleDerivative = "f'(x) = -\\frac{1}{1+x^2}"
            ),

            // Hyperbolic
            Formula(
                name = "Hyperbolic Sine",
                category = "Hyperbolic Functions",
                latex = "\\frac{d}{dx}(\\sinh(x)) = \\cosh(x)",
                explanation = "The derivative of the hyperbolic sine is the hyperbolic cosine.",
                exampleFunction = "f(x) = \\sinh(x)",
                exampleDerivative = "f'(x) = \\cosh(x)"
            ),
            Formula(
                name = "Hyperbolic Cosine",
                category = "Hyperbolic Functions",
                latex = "\\frac{d}{dx}(\\cosh(x)) = \\sinh(x)",
                explanation = "The derivative of the hyperbolic cosine is the hyperbolic sine.",
                exampleFunction = "f(x) = \\cosh(x)",
                exampleDerivative = "f'(x) = \\sinh(x)"
            ),
            Formula(
                name = "Hyperbolic Tangent",
                category = "Hyperbolic Functions",
                latex = "\\frac{d}{dx}(\\tanh(x)) = \\text{sech}^2(x)",
                explanation = "The derivative of the hyperbolic tangent is the hyperbolic secant squared.",
                exampleFunction = "f(x) = \\tanh(x)",
                exampleDerivative = "f'(x) = \\text{sech}^2(x)"
            ),
            Formula(
                name = "Hyperbolic Secant",
                category = "Hyperbolic Functions",
                latex = "\\frac{d}{dx}(\\text{sech}(x)) = -\\text{sech}(x)\\tanh(x)",
                explanation = "The derivative of the hyperbolic secant is negative hyperbolic secant times hyperbolic tangent.",
                exampleFunction = "f(x) = \\text{sech}(x)",
                exampleDerivative = "f'(x) = -\\text{sech}(x)\\tanh(x)"
            ),
            Formula(
                name = "Hyperbolic Cosecant",
                category = "Hyperbolic Functions",
                latex = "\\frac{d}{dx}(\\text{csch}(x)) = -\\text{csch}(x)\\coth(x)",
                explanation = "The derivative of the hyperbolic cosecant is negative hyperbolic cosecant times hyperbolic cotangent.",
                exampleFunction = "f(x) = \\text{csch}(x)",
                exampleDerivative = "f'(x) = -\\text{csch}(x)\\coth(x)"
            ),
            Formula(
                name = "Hyperbolic Cotangent",
                category = "Hyperbolic Functions",
                latex = "\\frac{d}{dx}(\\text{coth}(x)) = -\\text{csch}^2(x)",
                explanation = "The derivative of the hyperbolic cotangent is negative hyperbolic cosecant squared.",
                exampleFunction = "f(x) = \\text{coth}(x)",
                exampleDerivative = "f'(x) = -\\text{csch}^2(x)"
            )
        )
    }
}