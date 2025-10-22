package com.codewithmahad.derivativecalculator

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codewithmahad.derivativecalculator.data.HistoryItem
import com.codewithmahad.derivativecalculator.ui.components.MathPanel
import com.codewithmahad.derivativecalculator.ui.theme.DerivativeCalculatorTheme
import com.codewithmahad.derivativecalculator.viewmodels.BaseCalculatorViewModel
import com.codewithmahad.derivativecalculator.viewmodels.GradientViewModel
import com.codewithmahad.derivativecalculator.viewmodels.HistoryViewModel
import com.codewithmahad.derivativecalculator.viewmodels.LinearApproximationViewModel
import com.codewithmahad.derivativecalculator.viewmodels.PartialDerivativeViewModel
import com.codewithmahad.derivativecalculator.viewmodels.QuizViewModel
import com.codewithmahad.derivativecalculator.viewmodels.SettingsViewModel
import com.codewithmahad.derivativecalculator.viewmodels.VectorFieldViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val settingsViewModel = ViewModelProvider(this).get<SettingsViewModel>()
        val historyViewModel = ViewModelProvider(this).get<HistoryViewModel>()

        // Auto-clear history on launch
        lifecycleScope.launch {
            val autoClearSetting = settingsViewModel.historyAutoClear.first()
            if (autoClearSetting != HistoryAutoClear.NEVER) {
                val history = historyViewModel.history.first()
                val cutoff = System.currentTimeMillis() - autoClearSetting.toMillis()
                val filteredHistory = history.filter { it.timestamp >= cutoff }
                historyViewModel.clearAndSaveHistory(filteredHistory)
            }
        }

        // Keep the splash screen on until the theme is loaded
        var isThemeLoaded = false
        splashScreen.setKeepOnScreenCondition { !isThemeLoaded }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val themeOption by settingsViewModel.themeOption.collectAsState()

            // Signal that the theme is loaded
            LaunchedEffect(themeOption) {
                isThemeLoaded = true
            }

            DerivativeCalculatorTheme(themeOption = themeOption) {
                DerivifyApp(
                    settingsViewModel = settingsViewModel,
                    historyViewModel = historyViewModel
                )
            }
        }
    }
}

fun HistoryAutoClear.toMillis(): Long {
    return when (this) {
        HistoryAutoClear.NEVER -> -1
        HistoryAutoClear.ONE_DAY -> TimeUnit.DAYS.toMillis(1)
        HistoryAutoClear.ONE_WEEK -> TimeUnit.DAYS.toMillis(7)
        HistoryAutoClear.ONE_MONTH -> TimeUnit.DAYS.toMillis(30)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DerivifyApp(
    settingsViewModel: SettingsViewModel,
    historyViewModel: HistoryViewModel
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // ViewModel instances
    val partialDerivativeViewModel: PartialDerivativeViewModel = viewModel()
    val linearApproximationViewModel: LinearApproximationViewModel = viewModel()
    val gradientViewModel: GradientViewModel = viewModel()
    val vectorFieldViewModel: VectorFieldViewModel = viewModel()
    val quizViewModel: QuizViewModel = viewModel()

    // Determine the active ViewModel based on the current route
    val activeViewModel: BaseCalculatorViewModel = when (currentRoute) {
        AppRoutes.LINEAR_APPROXIMATION -> linearApproximationViewModel
        AppRoutes.GRADIENT -> gradientViewModel
        else -> partialDerivativeViewModel
    }

    var showMathPanel by remember { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current
    val hapticsEnabled by settingsViewModel.hapticFeedbackEnabled.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val keepScreenOn by settingsViewModel.keepScreenOn.collectAsState()
    val window = (LocalView.current.context as Activity).window
    SideEffect {
        if (keepScreenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    val topLevelScreens = listOf(
        AppRoutes.CALCULATOR, AppRoutes.HISTORY, AppRoutes.GRAPH,
        AppRoutes.GRADIENT, AppRoutes.DIRECTIONAL_DERIVATIVE, AppRoutes.MAXIMA_MINIMA,
        AppRoutes.LINEAR_APPROXIMATION, AppRoutes.FORMULA_SHEET, AppRoutes.ABOUT,
        AppRoutes.VECTOR_FIELD, AppRoutes.QUIZ
    )
    val isTopLevelScreen = currentRoute in topLevelScreens

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Calculus Toolkit",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Quiz, contentDescription = "Calculus Quiz") },
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Calculus Quiz")
                            Badge { Text("Fun") }
                        }
                    },
                    selected = currentRoute == "quiz",
                    onClick = {
                        navController.navigate("quiz"); scope.launch { drawerState.close() }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        unselectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f),
                        selectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        unselectedTextColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Calculate, contentDescription = "Calculator") },
                    label = { Text("Partial Derivative") },
                    selected = currentRoute == "calculator",
                    onClick = {
                        navController.navigate("calculator")
                        scope.launch { drawerState.close() }
                    })
                // Gradient Calculator
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.FilterTiltShift, contentDescription = "Gradient"
                        )
                    }, // Icon for Gradient
                    label = { Text("Gradient") }, selected = currentRoute == "gradient", onClick = {
                        navController.navigate("gradient"); scope.launch { drawerState.close() }
                    })

                // Directional Derivative
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.NorthEast, contentDescription = "Directional Derivative"
                        )
                    },
                    label = { Text("Directional Derivative") },
                    selected = currentRoute == "directional_derivative",
                    onClick = {
                        navController.navigate("directional_derivative"); scope.launch { drawerState.close() }
                    })

                // Maxima & Minima Test
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.Landscape, contentDescription = "Maxima & Minima"
                        )
                    },
                    label = { Text("Maxima & Minima Test") },
                    selected = currentRoute == "maxima_minima",
                    onClick = {
                        navController.navigate("maxima_minima"); scope.launch { drawerState.close() }
                    })

                // Linear Approximation
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.LinearScale, contentDescription = "Linear Approximation"
                        )
                    },
                    label = { Text("Linear Approximation") },
                    selected = currentRoute == "linear_approximation",
                    onClick = {
                        navController.navigate("linear_approximation"); scope.launch { drawerState.close() }
                    })
                // Vector Field
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.Default.Grain, contentDescription = "Vector Field"
                        )
                    },
                    label = { Text("Vector Field") },
                    selected = currentRoute == "vector_field",
                    onClick = {
                        navController.navigate("vector_field"); scope.launch { drawerState.close() }
                    })
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Formula Sheet") },
                    label = { Text("Formula Sheet") },
                    selected = currentRoute == "formula_sheet",
                    onClick = {
                        navController.navigate("formula_sheet")
                        scope.launch { drawerState.close() }
                    })
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "About Developer") },
                    label = { Text("About Developer") },
                    selected = currentRoute == "about",
                    onClick = {
                        navController.navigate("about")
                        scope.launch { drawerState.close() }
                    })
            }
        }) {
        Column(modifier = Modifier.fillMaxSize().imePadding()) {
            Scaffold(
                modifier = Modifier.weight(1f),
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    val title = when (currentRoute) {
                        AppRoutes.ABOUT -> "About Developer"
                        AppRoutes.FORMULA_SHEET -> "Formula Sheet"
                        AppRoutes.SETTINGS -> "Settings"
                        AppRoutes.GRADIENT -> "Gradient Calculator"
                        AppRoutes.LINEAR_APPROXIMATION -> "Linear Approximation"
                        AppRoutes.SOLUTION_STEPS -> "Solution Steps"
                        AppRoutes.VECTOR_FIELD -> "Vector Field Explorer"
                        AppRoutes.QUIZ -> "Calculus Quiz"
                        else -> "Derivify"
                    }
                    TopAppBar(
                        title = { Text(title) },
                        navigationIcon = {
                            if (isTopLevelScreen) {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) { Icon(Icons.Default.Menu, "Menu") }
                            } else {
                                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                            }
                        },
                        actions = {
                            if (isTopLevelScreen) {
                                IconButton(onClick = { navController.navigate(AppRoutes.SETTINGS) }) { Icon(Icons.Default.Settings, "Settings") }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                        )
                    )
                },
                bottomBar = {
                    // The Bottom Nav Bar is ONLY visible when the Math Panel is hidden.
                    AnimatedVisibility(visible = !showMathPanel) {
                        val showBottomBar = currentRoute in listOf(AppRoutes.CALCULATOR, AppRoutes.HISTORY, AppRoutes.GRAPH)
                        if (showBottomBar) {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Calculate, "Calculator") },
                                    label = { Text("Calculator") },
                                    selected = currentRoute == AppRoutes.CALCULATOR,
                                    onClick = {
                                        if (hapticsEnabled) { haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove) }
                                        navController.navigate(AppRoutes.CALCULATOR) { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedTextColor = MaterialTheme.colorScheme.primary, // Ensures visibility
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.History, "History") },
                                    label = { Text("History") },
                                    selected = currentRoute == AppRoutes.HISTORY,
                                    onClick = {
                                        if (hapticsEnabled) { haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove) }
                                        navController.navigate(AppRoutes.HISTORY) { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Timeline, "Graph") },
                                    label = { Text("Graph") },
                                    selected = currentRoute == AppRoutes.GRAPH,
                                    onClick = {
                                        if (hapticsEnabled) { haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove) }
                                        navController.navigate(AppRoutes.GRAPH) { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                AppNavigation(
                    navController = navController,
                    partialDerivativeViewModel = partialDerivativeViewModel,
                    linearApproximationViewModel = linearApproximationViewModel,
                    gradientViewModel = gradientViewModel,
                    settingsViewModel = settingsViewModel,
                    historyViewModel = historyViewModel,
                    vectorFieldViewModel = vectorFieldViewModel,
                    quizViewModel = quizViewModel,
                    onMathInputFocused = { showMathPanel = it },
                    contentPadding = innerPadding,
                    snackbarHostState = snackbarHostState,
                    scope = scope
                )
            }
            AnimatedVisibility(visible = showMathPanel) {
                MathPanel(calculatorViewModel = activeViewModel)
            }
        }
    }
}
