package com.codewithmahad.derivativecalculator

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.codewithmahad.derivativecalculator.data.Formula
import com.codewithmahad.derivativecalculator.ui.components.InProgressScreen
import com.codewithmahad.derivativecalculator.ui.screens.AboutScreen
import com.codewithmahad.derivativecalculator.ui.screens.CalculatorScreen
import com.codewithmahad.derivativecalculator.ui.screens.FormulaDetailScreen
import com.codewithmahad.derivativecalculator.ui.screens.FormulaSheetScreen
import com.codewithmahad.derivativecalculator.ui.screens.GradientScreen
import com.codewithmahad.derivativecalculator.ui.screens.GraphScreen
import com.codewithmahad.derivativecalculator.ui.screens.HistoryScreen
import com.codewithmahad.derivativecalculator.ui.screens.LinearApproximationScreen
import com.codewithmahad.derivativecalculator.ui.screens.QuizScreen
import com.codewithmahad.derivativecalculator.ui.screens.SettingsScreen
import com.codewithmahad.derivativecalculator.ui.screens.SolutionStepScreen
import com.codewithmahad.derivativecalculator.ui.screens.VectorFieldScreen
import com.codewithmahad.derivativecalculator.viewmodels.GradientViewModel
import com.codewithmahad.derivativecalculator.viewmodels.HistoryViewModel
import com.codewithmahad.derivativecalculator.viewmodels.LinearApproximationViewModel
import com.codewithmahad.derivativecalculator.viewmodels.PartialDerivativeViewModel
import com.codewithmahad.derivativecalculator.viewmodels.QuizViewModel
import com.codewithmahad.derivativecalculator.viewmodels.SettingsViewModel
import com.codewithmahad.derivativecalculator.viewmodels.VectorFieldViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.CoroutineScope


object AppRoutes {
    const val CALCULATOR = "calculator"
    const val HISTORY = "history"
    const val GRAPH = "graph"
    const val ABOUT = "about"
    const val FORMULA_SHEET = "formula_sheet"
    const val FORMULA_DETAIL = "formula_detail"
    const val GRADIENT = "gradient"
    const val DIRECTIONAL_DERIVATIVE = "directional_derivative"
    const val MAXIMA_MINIMA = "maxima_minima"
    const val LINEAR_APPROXIMATION = "linear_approximation"
    const val SETTINGS = "settings"
    const val SOLUTION_STEPS = "solution_steps"
    const val VECTOR_FIELD = "vector_field"
    const val QUIZ = "quiz"
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    navController: NavHostController,
    partialDerivativeViewModel: PartialDerivativeViewModel,
    linearApproximationViewModel: LinearApproximationViewModel,
    gradientViewModel: GradientViewModel,
    settingsViewModel: SettingsViewModel,
    historyViewModel: HistoryViewModel,
    vectorFieldViewModel: VectorFieldViewModel,
    quizViewModel: QuizViewModel,
    onMathInputFocused: (Boolean) -> Unit,
    contentPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = AppRoutes.CALCULATOR,
        modifier = Modifier,
        enterTransition = { fadeIn(animationSpec = tween(350)) },
        exitTransition = { fadeOut(animationSpec = tween(350)) }
    ) {
        // The main calculator screen.
        composable(AppRoutes.CALCULATOR) {
            CalculatorScreen(
                viewModel = partialDerivativeViewModel,
                onMathInputFocused = onMathInputFocused,
                contentPadding = contentPadding,
                snackbarHostState = snackbarHostState,
                scope = scope,
                onShowSteps = { navController.navigate(AppRoutes.SOLUTION_STEPS) }
            )
        }
        // The history screen.
        composable(AppRoutes.HISTORY) {
            HistoryScreen(
                viewModel = historyViewModel,
                onItemClicked = { item ->
                    partialDerivativeViewModel.loadFromHistory(item)
                    navController.navigate(AppRoutes.CALCULATOR) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                contentPadding = contentPadding,
                snackbarHostState = snackbarHostState,
                scope = scope
            )
        }
        // The graph screen.
        composable(AppRoutes.GRAPH) {
            GraphScreen(
                viewModel = partialDerivativeViewModel,
                contentPadding = contentPadding
            )
        }
        // The about screen.
        composable(AppRoutes.ABOUT) {
            AboutScreen(contentPadding = contentPadding)
        }
        // The formula sheet screen.
        composable(AppRoutes.FORMULA_SHEET) {
            FormulaSheetScreen(
                contentPadding = contentPadding,
                onFormulaClick = { formula ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("formula", formula)
                    navController.navigate(AppRoutes.FORMULA_DETAIL)
                }
            )
        }
        // The settings screen.
        composable(AppRoutes.SETTINGS) {
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                contentPadding = contentPadding
            )
        }
        // The gradient calculator screen.
        composable(AppRoutes.GRADIENT) {
            GradientScreen(
                viewModel = gradientViewModel,
                onMathInputFocused = onMathInputFocused,
                contentPadding = contentPadding,
                snackbarHostState = snackbarHostState,
                scope = scope
            )
        }
        // The linear approximation screen.
        composable(AppRoutes.LINEAR_APPROXIMATION) {
            LinearApproximationScreen(
                viewModel = linearApproximationViewModel,
                onMathInputFocused = onMathInputFocused,
                contentPadding = contentPadding,
                snackbarHostState = snackbarHostState,
                scope = scope
            )
        }
        // In-progress screens.
        composable(AppRoutes.DIRECTIONAL_DERIVATIVE) { InProgressScreen("Directional Derivative", contentPadding) }
        composable(AppRoutes.MAXIMA_MINIMA) { InProgressScreen("Maxima & Minima Test", contentPadding) }

        // The formula detail screen, with a custom slide-in animation
        composable(
            route = AppRoutes.FORMULA_DETAIL,
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(350)) }
        ) {
            val formula = navController.previousBackStackEntry?.savedStateHandle?.get<Formula>("formula")
            if (formula != null) {
                FormulaDetailScreen(
                    formula = formula,
                    contentPadding = contentPadding
                )
            }
        }

        composable(AppRoutes.SOLUTION_STEPS) {
            SolutionStepScreen(
                steps = partialDerivativeViewModel.calculationSteps,
                contentPadding = contentPadding
            )
        }
        
        composable(AppRoutes.VECTOR_FIELD) {
            VectorFieldScreen(
                contentPadding = contentPadding,
                viewModel = vectorFieldViewModel
            )
        }

        composable(AppRoutes.QUIZ) {
            QuizScreen(
                contentPadding = contentPadding,
                quizViewModel = quizViewModel
            )
        }
    }
}