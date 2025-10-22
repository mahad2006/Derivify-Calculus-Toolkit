package com.codewithmahad.derivativecalculator.data

import androidx.compose.runtime.Immutable
import kotlin.random.Random

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
    NIGHTMARE
}

@Immutable
data class QuizQuestion(
    val function: String,
    val options: List<String>,
    val correctAnswer: String,
    val rule: String,
    val difficulty: Difficulty
) {
    // Shuffling is now done in the ViewModel to ensure it's different
    // every time, not just once when the app starts.
    // We remove shuffledOptions from here.
}

object QuizData {

    // The new getQuestions function.
    // It takes a difficulty and a count (which you'll set to 10).
    // It shuffles the list and takes the first 10.
    fun getQuestions(difficulty: Difficulty, count: Int): List<QuizQuestion> {
        val questionBank = when (difficulty) {
            Difficulty.EASY -> easyQuestions
            Difficulty.MEDIUM -> mediumQuestions
            Difficulty.HARD -> hardQuestions
            Difficulty.NIGHTMARE -> nightmareQuestions
        }

        // Shuffle the chosen bank and take the requested number of questions
        return questionBank.shuffled(Random(System.nanoTime())).take(count)
    }

    // EASY: Basic rules (Calc I)
    private val easyQuestions = listOf(
        QuizQuestion(
            function = "f(x) = x^5",
            options = listOf("5x^4", "5x^5", "x^4", "4x^5"),
            correctAnswer = "5x^4",
            rule = "Power Rule",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = \\cos(x)",
            options = listOf("-\\sin(x)", "\\sin(x)", "-\\cos(x)", "\\cos(x)"),
            correctAnswer = "-\\sin(x)",
            rule = "Trigonometric",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = e^x",
            options = listOf("e^x", "x*e^(x-1)", "\\ln(x)", "1"),
            correctAnswer = "e^x",
            rule = "Exponential",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = \\ln(x)",
            options = listOf("\\frac{1}{x}", "x", "\\frac{1}{\\ln(x)}", "e^x"),
            correctAnswer = "\\frac{1}{x}",
            rule = "Logarithmic",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = 7",
            options = listOf("0", "7", "1", "x"),
            correctAnswer = "0",
            rule = "Constant Rule",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = 5x^3 - 2x + 1",
            options = listOf("15x^2 - 2", "15x - 2", "5x^2 - 2", "15x^2"),
            correctAnswer = "15x^2 - 2",
            rule = "Polynomial",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = \\sqrt{x}",
            options = listOf("\\frac{1}{2\\sqrt{x}}", "\\frac{1}{\\sqrt{x}}", "2\\sqrt{x}", "\\frac{x}{2}"),
            correctAnswer = "\\frac{1}{2\\sqrt{x}}",
            rule = "Power Rule",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = x \\sin(x)",
            options = listOf("\\sin(x) + x\\cos(x)", "x\\cos(x)", "\\cos(x)", "1 + \\cos(x)"),
            correctAnswer = "\\sin(x) + x\\cos(x)",
            rule = "Product Rule",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = \\frac{x}{x+1}",
            options = listOf("\\frac{1}{(x+1)^2}", "\\frac{x}{(x+1)^2}", "1", "\\frac{-1}{(x+1)^2}"),
            correctAnswer = "\\frac{1}{(x+1)^2}",
            rule = "Quotient Rule",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = (2x+1)^3",
            options = listOf("6(2x+1)^2", "3(2x+1)^2", "2(2x+1)^2", "6x^2"),
            correctAnswer = "6(2x+1)^2",
            rule = "Chain Rule",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = e^{3x}",
            options = listOf("3e^{3x}", "e^{3x}", "3xe^{3x-1}", "\\ln(3x)"),
            correctAnswer = "3e^{3x}",
            rule = "Chain Rule",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = \\sin(x^2)",
            options = listOf("2x\\cos(x^2)", "\\cos(x^2)", "-2x\\cos(x^2)", "2\\sin(x)\\cos(x)"),
            correctAnswer = "2x\\cos(x^2)",
            rule = "Chain Rule",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = \\tan(x)",
            options = listOf("\\sec^2(x)", "\\cot(x)", "-\\sec^2(x)", "\\sec(x)\\tan(x)"),
            correctAnswer = "\\sec^2(x)",
            rule = "Trigonometric",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = 10^x",
            options = listOf("10^x \\ln(10)", "x 10^{x-1}", "10^x", "\\frac{10^x}{\\ln(10)}"),
            correctAnswer = "10^x \\ln(10)",
            rule = "Exponential",
            difficulty = Difficulty.EASY
        ),
        QuizQuestion(
            function = "f(x) = \\frac{1}{x}",
            options = listOf("-\\frac{1}{x^2}", "\\ln(x)", "-1", "-x^2"),
            correctAnswer = "-\\frac{1}{x^2}",
            rule = "Power Rule",
            difficulty = Difficulty.EASY
        )
    )

    // MEDIUM: Implicit, Logarithmic, Inverse Trig, complex combinations
    private val mediumQuestions = listOf(
        QuizQuestion(
            function = "f(x) = x^x",
            options = listOf("x^x (1 + \\ln(x))", "x \\cdot x^{x-1}", "x^x", "e^{x\\ln(x)}"),
            correctAnswer = "x^x (1 + \\ln(x))",
            rule = "Logarithmic Diff.",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "f(x) = \\arcsin(x)",
            options = listOf("\\frac{1}{\\sqrt{1-x^2}}", "\\arccos(x)", "\\frac{1}{1+x^2}", "-\\frac{1}{\\sqrt{1-x^2}}"),
            correctAnswer = "\\frac{1}{\\sqrt{1-x^2}}",
            rule = "Inverse Trig",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "f(x) = \\arctan(x^2)",
            options = listOf("\\frac{2x}{1+x^4}", "\\frac{1}{1+x^4}", "\\frac{2x}{1+x^2}", "2x \\arctan(x)"),
            correctAnswer = "\\frac{2x}{1+x^4}",
            rule = "Inverse Trig",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "f(x) = \\ln(\\sin(x))",
            options = listOf("\\cot(x)", "\\frac{1}{\\sin(x)}", "\\cos(x)", "\\tan(x)"),
            correctAnswer = "\\cot(x)",
            rule = "Chain Rule",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "Find dy/dx: x^2 + y^2 = 9",
            options = listOf("-\\frac{x}{y}", "\\frac{x}{y}", "-2x", "2x+2y"),
            correctAnswer = "-\\frac{x}{y}",
            rule = "Implicit Diff.",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "f(x) = e^{\\sin(x)}",
            options = listOf("\\cos(x) e^{\\sin(x)}", "e^{\\sin(x)}", "e^{\\cos(x)}", "\\sin(x) e^{\\cos(x)}"),
            correctAnswer = "\\cos(x) e^{\\sin(x)}",
            rule = "Chain Rule",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "f(x) = \\sin(e^x)",
            options = listOf("e^x \\cos(e^x)", "\\cos(e^x)", "e^x \\sin(e^x)", "-e^x \\cos(e^x)"),
            correctAnswer = "e^x \\cos(e^x)",
            rule = "Chain Rule",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "f(x) = \\sec(x)",
            options = listOf("\\sec(x)\\tan(x)", "\\tan^2(x)", "-\\csc(x)\\cot(x)", "\\sec^2(x)"),
            correctAnswer = "\\sec(x)\\tan(x)",
            rule = "Trigonometric",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "Find dy/dx: xy = 1",
            options = listOf("-\\frac{y}{x}", "\\frac{y}{x}", "-xy", "1"),
            correctAnswer = "-\\frac{y}{x}",
            rule = "Implicit Diff.",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "f(x) = \\frac{e^x}{x^2}",
            options = listOf("\\frac{e^x(x-2)}{x^3}", "\\frac{e^x}{2x}", "\\frac{e^x(x^2-2x)}{x^4}", "e^x(x-2)"),
            correctAnswer = "\\frac{e^x(x-2)}{x^3}",
            rule = "Quotient Rule",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "f(x) = \\cosh(x)",
            options = listOf("\\sinh(x)", "-\\sinh(x)", "\\cosh(x)", "e^x"),
            correctAnswer = "\\sinh(x)",
            rule = "Hyperbolic",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "f(x) = x^2 \\ln(x)",
            options = listOf("2x\\ln(x) + x", "2x\\ln(x) + x^2", "2x + \\frac{1}{x}", "2x / x"),
            correctAnswer = "2x\\ln(x) + x",
            rule = "Product Rule",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "f(x) = \\sqrt{x^2+1}",
            options = listOf("\\frac{x}{\\sqrt{x^2+1}}", "\\frac{1}{2\\sqrt{x^2+1}}", "2x\\sqrt{x^2+1}", "\\frac{2x}{\\sqrt{x^2+1}}"),
            correctAnswer = "\\frac{x}{\\sqrt{x^2+1}}",
            rule = "Chain Rule",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "f(x) = \\frac{\\ln(x)}{x}",
            options = listOf("\\frac{1 - \\ln(x)}{x^2}", "\\frac{1}{x^2}", "\\frac{\\ln(x)-1}{x^2}", "-\\frac{\\ln(x)}{x^2}"),
            correctAnswer = "\\frac{1 - \\ln(x)}{x^2}",
            rule = "Quotient Rule",
            difficulty = Difficulty.MEDIUM
        ),
        QuizQuestion(
            function = "f(x) = \\sin^3(x)",
            options = listOf("3\\sin^2(x)\\cos(x)", "3\\cos^3(x)", "3\\sin^2(x)", "\\cos^3(x)"),
            correctAnswer = "3\\sin^2(x)\\cos(x)",
            rule = "Chain Rule",
            difficulty = Difficulty.MEDIUM
        )
    )

    // HARD: Partial Derivatives, Gradients, complex implicit
    private val hardQuestions = listOf(
        QuizQuestion(
            function = "f(x,y) = x^2y + \\sin(y)",
            options = listOf("f_x = 2xy, f_y = x^2 + \\cos(y)", "f_x = 2x, f_y = \\cos(y)", "f_x = 2xy, f_y = x^2 - \\cos(y)", "f_x = x^2, f_y = \\cos(y)"),
            correctAnswer = "f_x = 2xy, f_y = x^2 + \\cos(y)",
            rule = "Partial Derivative",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "f(x,y) = e^{xy}",
            options = listOf("f_x = ye^{xy}, f_y = xe^{xy}", "f_x = e^{xy}, f_y = e^{xy}", "f_x = ye^{xy}, f_y = ye^{xy}", "f_x = xe^{xy}, f_y = ye^{xy}"),
            correctAnswer = "f_x = ye^{xy}, f_y = xe^{xy}",
            rule = "Partial Derivative",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "Find \\nabla f for f(x,y) = x^3 - 3xy + y^3",
            options = listOf("\\langle 3x^2 - 3y, -3x + 3y^2 \\rangle", "\\langle 3x^2, 3y^2 \\rangle", "\\langle 3x^2 - 3y, 3y^2 \\rangle", "\\langle 3x^2 - 3, -3 + 3y^2 \\rangle"),
            correctAnswer = "\\langle 3x^2 - 3y, -3x + 3y^2 \\rangle",
            rule = "Gradient",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "Find dy/dx: \\sin(x+y) = y^2 \\cos(x)",
            options = listOf("\\frac{y^2\\sin(x) + \\cos(x+y)}{2y\\cos(x) - \\cos(x+y)}", "\\frac{\\cos(x+y)}{2y\\cos(x)}", "\\frac{y^2\\sin(x)}{\\cos(x+y)}", "\\frac{1}{2y\\cos(x)}"),
            correctAnswer = "\\frac{y^2\\sin(x) + \\cos(x+y)}{2y\\cos(x) - \\cos(x+y)}",
            rule = "Implicit Diff.",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "f(x) = x^{\\sin(x)}",
            options = listOf("x^{\\sin(x)} (\\cos(x)\\ln(x) + \\frac{\\sin(x)}{x})", "x^{\\sin(x)} \\cos(x) \\ln(x)", "\\cos(x) x^{\\sin(x)-1}", "x^{\\sin(x)} (\\frac{\\cos(x)}{x} + \\ln(x))"),
            correctAnswer = "x^{\\sin(x)} (\\cos(x)\\ln(x) + \\frac{\\sin(x)}{x})",
            rule = "Logarithmic Diff.",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "f(x,y) = \\frac{x}{y}",
            options = listOf("f_x = \\frac{1}{y}, f_y = -\\frac{x}{y^2}", "f_x = 1, f_y = -1", "f_x = y, f_y = x", "f_x = \\frac{1}{y}, f_y = \\frac{x}{y^2}"),
            correctAnswer = "f_x = \\frac{1}{y}, f_y = -\\frac{x}{y^2}",
            rule = "Partial Derivative",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "f(x) = \\ln(\\sec(x) + \\tan(x))",
            options = listOf("\\sec(x)", "\\tan(x)", "\\frac{1}{\\sec(x) + \\tan(x)}", "\\cos(x)"),
            correctAnswer = "\\sec(x)",
            rule = "Chain Rule",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "f(x) = e^{x^2} \\ln(x)",
            options = listOf("e^{x^2}(2x\\ln(x) + \\frac{1}{x})", "e^{x^2}(\\frac{1}{x})", "2xe^{x^2}\\ln(x)", "\\frac{2x e^{x^2}}{x}"),
            correctAnswer = "e^{x^2}(2x\\ln(x) + \\frac{1}{x})",
            rule = "Product Rule",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "f(x,y) = x^y",
            options = listOf("f_x = yx^{y-1}, f_y = x^y \\ln(x)", "f_x = yx^{y-1}, f_y = yx^{y-1}", "f_x = x^y \\ln(x), f_y = yx^{y-1}", "f_x = yx^{y-1}, f_y = x^y"),
            correctAnswer = "f_x = yx^{y-1}, f_y = x^y \\ln(x)",
            rule = "Partial Derivative",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "Find \\nabla f for f(x,y) = \\ln(x^2 + y^2)",
            options = listOf("\\langle \\frac{2x}{x^2+y^2}, \\frac{2y}{x^2+y^2} \\rangle", "\\langle \\frac{1}{x^2+y^2}, \\frac{1}{x^2+y^2} \\rangle", "\\langle \\frac{2x}{x}, \\frac{2y}{y} \\rangle", "\\langle 2x, 2y \\rangle"),
            correctAnswer = "\\langle \\frac{2x}{x^2+y^2}, \\frac{2y}{x^2+y^2} \\rangle",
            rule = "Gradient",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "f(x,y,z) = xyz",
            options = listOf("f_x = yz, f_y = xz, f_z = xy", "f_x = 1, f_y = 1, f_z = 1", "f_x = yz, f_y = yz, f_z = yz", "\\nabla f = \\langle x,y,z \\rangle"),
            correctAnswer = "f_x = yz, f_y = xz, f_z = xy",
            rule = "Partial Derivative",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "f(x) = \\arccos(e^x)",
            options = listOf("-\\frac{e^x}{\\sqrt{1-e^{2x}}}", "\\frac{e^x}{\\sqrt{1-e^{2x}}}", "\\frac{1}{\\sqrt{1-e^{2x}}}", "-\\frac{1}{\\sqrt{1-e^{2x}}}"),
            correctAnswer = "-\\frac{e^x}{\\sqrt{1-e^{2x}}}",
            rule = "Inverse Trig",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "f(x,y) = \\arctan(\\frac{y}{x})",
            options = listOf("f_x = \\frac{-y}{x^2+y^2}, f_y = \\frac{x}{x^2+y^2}", "f_x = \\frac{1}{1+(y/x)^2}, f_y = \\frac{1}{1+(y/x)^2}", "f_x = \\frac{y}{x^2+y^2}, f_y = \\frac{x}{x^2+y^2}", "f_x = \\frac{-y}{x^2}, f_y = \\frac{1}{x}"),
            correctAnswer = "f_x = \\frac{-y}{x^2+y^2}, f_y = \\frac{x}{x^2+y^2}",
            rule = "Partial Derivative",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "Find dy/dx: x^y = y^x",
            options = listOf("\\frac{y^2 - yx\\ln(y)}{x^2 - xy\\ln(x)}", "\\frac{y}{x}", "\\frac{\\ln(y)}{\\ln(x)}", "1"),
            correctAnswer = "\\frac{y^2 - yx\\ln(y)}{x^2 - xy\\ln(x)}",
            rule = "Implicit Diff.",
            difficulty = Difficulty.HARD
        ),
        QuizQuestion(
            function = "f(x) = \\tanh(x^2)",
            options = listOf("2x \\text{sech}^2(x^2)", "\\text{sech}^2(x^2)", "2x \\sinh(x^2)", "2x \\coth(x^2)"),
            correctAnswer = "2x \\text{sech}^2(x^2)",
            rule = "Hyperbolic",
            difficulty = Difficulty.HARD
        )
    )

    // NIGHTMARE: Higher-order, Leibniz rule, complex gradients
    private val nightmareQuestions = listOf(
        QuizQuestion(
            function = "Find f_xy for f(x,y) = x^y",
            options = listOf("x^{y-1} (1 + y\\ln(x))", "yx^{y-1} \\ln(x)", "y(y-1)x^{y-2}", "x^y \\ln(x)"),
            correctAnswer = "x^{y-1} (1 + y\\ln(x))",
            rule = "Mixed Partial",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "Find f_yx for f(x,y) = \\sin(x^2y)",
            options = listOf("2xy^2\\cos(x^2y) - 2x\\sin(x^2y)", "2xy^2\\cos(x^2y)", "2x\\cos(x^2y) - 2x^3y\\sin(x^2y)", "2x\\cos(x^2y)"),
            correctAnswer = "2x\\cos(x^2y) - 2x^3y\\sin(x^2y)",
            rule = "Mixed Partial",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "f(x) = \\frac{d}{dx} \\int_{0}^{x} e^{-t^2} dt",
            options = listOf("e^{-x^2}", "-2xe^{-x^2}", "e^{-x^2} - 1", "0"),
            correctAnswer = "e^{-x^2}",
            rule = "FTC Part 1",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "f(x) = \\frac{d}{dx} \\int_{1}^{x^2} \\cos(t) dt",
            options = listOf("2x \\cos(x^2)", "\\cos(x^2)", "2x\\cos(x^2) - \\cos(1)", "-\\sin(x^2)"),
            correctAnswer = "2x \\cos(x^2)",
            rule = "Leibniz Rule",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "Find D_u f(1,1) for f(x,y) = x^2+y^2, u = \\langle \\frac{1}{\\sqrt{2}}, \\frac{1}{\\sqrt{2}} \\rangle",
            options = listOf("2\\sqrt{2}", "\\sqrt{2}", "4", "2"),
            correctAnswer = "2\\sqrt{2}",
            rule = "Directional Derivative",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "f(x) = \\frac{(x+1)^2 \\sqrt{x-2}}{(x+4)^3}",
            options = listOf("f(x) [\\frac{2}{x+1} + \\frac{1}{2(x-2)} - \\frac{3}{x+4}]", "f(x) [\\frac{2}{x+1} - \\frac{1}{2(x-2)} + \\frac{3}{x+4}]", "\\frac{2(x+1)}{3(x+4)^2}", "0"),
            correctAnswer = "f(x) [\\frac{2}{x+1} + \\frac{1}{2(x-2)} - \\frac{3}{x+4}]",
            rule = "Logarithmic Diff.",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "Find dy/dx at (1,1): x^3 + y^3 = 6xy",
            options = listOf("-1", "1", "0", "Not defined"),
            correctAnswer = "-1",
            rule = "Implicit Diff.",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "Find \\nabla f for f(x,y,z) = x e^{y^2 + z^2}",
            options = listOf("\\langle e^{y^2+z^2}, 2xy e^{y^2+z^2}, 2xz e^{y^2+z^2} \\rangle", "\\langle e^{y^2+z^2}, e^{y^2+z^2}, e^{y^2+z^2} \\rangle", "\\langle 1, 2y, 2z \\rangle", "\\langle e^{y^2+z^2}, 2xy, 2xz \\rangle"),
            correctAnswer = "\\langle e^{y^2+z^2}, 2xy e^{y^2+z^2}, 2xz e^{y^2+z^2} \\rangle",
            rule = "Gradient (3D)",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "Find f_xx for f(x,y) = \\ln(x^2+y^2)",
            options = listOf("\\frac{2(y^2-x^2)}{(x^2+y^2)^2}", "\\frac{-4xy}{(x^2+y^2)^2}", "\\frac{2x^2}{(x^2+y^2)^2}", "\\frac{2}{x^2+y^2}"),
            correctAnswer = "\\frac{2(y^2-x^2)}{(x^2+y^2)^2}",
            rule = "Second Partial",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "f(x) = \\frac{d}{dx} \\int_{x}^{x^2} \\frac{1}{t} dt",
            options = listOf("\\frac{1}{x}", "2x", "\\frac{2}{x^2} - \\frac{1}{x}", "0"),
            correctAnswer = "\\frac{1}{x}",
            rule = "Leibniz Rule",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "Find D_u f(0,0) for f(x,y) = e^x \\sin(y), u = \\langle \\frac{3}{5}, \\frac{4}{5} \\rangle",
            options = listOf("\\frac{4}{5}", "\\frac{3}{5}", "\\frac{7}{5}", "1"),
            correctAnswer = "\\frac{4}{5}",
            rule = "Directional Derivative",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "Find f_xy for f(x,y) = \\arctan(\\frac{y}{x})",
            options = listOf("\\frac{y^2-x^2}{(x^2+y^2)^2}", "\\frac{x^2-y^2}{(x^2+y^2)^2}", "\\frac{1}{(x^2+y^2)^2}", "0"),
            correctAnswer = "\\frac{y^2-x^2}{(x^2+y^2)^2}",
            rule = "Mixed Partial",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "f(r, \\theta) = (r\\cos\\theta, r\\sin\\theta). Find the Jacobian.",
            options = listOf("r", "1", "r^2", "0"),
            correctAnswer = "r",
            rule = "Jacobian",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "f(x) = \\sinh^{-1}(x)",
            options = listOf("\\frac{1}{\\sqrt{x^2+1}}", "\\frac{1}{\\sqrt{x^2-1}}", "\\frac{1}{1-x^2}", "\\cosh(x)"),
            correctAnswer = "\\frac{1}{\\sqrt{x^2+1}}",
            rule = "Inverse Hyperbolic",
            difficulty = Difficulty.NIGHTMARE
        ),
        QuizQuestion(
            function = "Find \\frac{d^2y}{dx^2}: x=t^2, y=t^3",
            options = listOf("\\frac{3}{4t}", "\\frac{3}{2t}", "6t", "\\frac{3t}{4}"),
            correctAnswer = "\\frac{3}{4t}",
            rule = "Parametric Diff.",
            difficulty = Difficulty.NIGHTMARE
        )
    )
}