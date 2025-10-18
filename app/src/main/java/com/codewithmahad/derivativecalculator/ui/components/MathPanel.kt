package com.codewithmahad.derivativecalculator.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codewithmahad.derivativecalculator.data.MathPanelTab
import com.codewithmahad.derivativecalculator.data.MathSymbol
import com.codewithmahad.derivativecalculator.viewmodels.BaseCalculatorViewModel
import com.codewithmahad.derivativecalculator.viewmodels.SettingsViewModel

@Composable
fun MathButtonGrid(
    symbols: List<MathSymbol>,
    onSymbolClick: (String) -> Unit,
    haptics: HapticFeedback,
    hapticsEnabled: Boolean
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 12.dp), // No vertical padding
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(symbols) { symbol ->
            OutlinedButton(
                onClick = {
                    onSymbolClick(symbol.insertText)
                    if (hapticsEnabled) {
                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.height(44.dp)
            ) {
                Text(text = symbol.displayText, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun OperatorBar(
    onSymbolClick: (String) -> Unit,
    haptics: HapticFeedback,
    hapticsEnabled: Boolean
) {
    val operators = listOf(
        MathSymbol("+"), MathSymbol("-"), MathSymbol("*"), MathSymbol("/"),
        MathSymbol("^"), MathSymbol("("), MathSymbol(")")
    )
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp), // No vertical padding
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            operators.forEach { symbol ->
                OutlinedButton(
                    onClick = {
                        onSymbolClick(symbol.insertText)
                        if (hapticsEnabled) {
                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        }
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = symbol.displayText, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun MathPanel(
    calculatorViewModel: BaseCalculatorViewModel,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(MathPanelTab.TRIG) }
    val haptics = LocalHapticFeedback.current
    val hapticsEnabled by settingsViewModel.hapticFeedbackEnabled.collectAsState()

    val onSymbolClick = { symbol: String ->
        calculatorViewModel.onMathSymbolClicked(symbol)
    }

    val trigFunctions = listOf(
        MathSymbol("sin", "sin()"), MathSymbol("cos", "cos()"), MathSymbol("tan", "tan()"),
        MathSymbol("csc", "csc()"), MathSymbol("sec", "sec()"), MathSymbol("cot", "cot()")
    )
    val hyperbolicFunctions = listOf(
        MathSymbol("sinh", "sinh()"), MathSymbol("cosh", "cosh()"), MathSymbol("tanh", "tanh()"),
        MathSymbol("csch", "csch()"), MathSymbol("sech", "sech()"), MathSymbol("coth", "coth()")
    )
    val inverseFunctions = listOf(
        MathSymbol("asin", "asin()"), MathSymbol("acos", "acos()"), MathSymbol("atan", "atan()"),
        MathSymbol("acsc", "acsc()"), MathSymbol("asec", "asec()"), MathSymbol("acot", "acot()")
    )
    val logFunctions = listOf(
        MathSymbol("ln", "ln()"), MathSymbol("exp", "exp()"), MathSymbol("sqrt", "sqrt()"),
        MathSymbol("abs", "abs()")
    )
    val symbolFunctions = listOf(
        MathSymbol("π"), MathSymbol("e")
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp) // Single point of control for vertical spacing
        ) {
            TabRow(
                selectedTabIndex = selectedTab.ordinal
            ) {
                MathPanelTab.values().forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab.title) },
                        modifier = Modifier.height(40.dp) // Reduced height for the tabs
                    )
                }
            }

            when (selectedTab) {
                MathPanelTab.TRIG -> MathButtonGrid(trigFunctions, onSymbolClick, haptics, hapticsEnabled)
                MathPanelTab.INVERSE -> MathButtonGrid(inverseFunctions, onSymbolClick, haptics, hapticsEnabled)
                MathPanelTab.HYPERBOLIC -> MathButtonGrid(hyperbolicFunctions, onSymbolClick, haptics, hapticsEnabled)
                MathPanelTab.LOGS -> MathButtonGrid(logFunctions, onSymbolClick, haptics, hapticsEnabled)
                MathPanelTab.SYMBOLS -> MathButtonGrid(symbolFunctions, onSymbolClick, haptics, hapticsEnabled)
            }
            OperatorBar(onSymbolClick, haptics, hapticsEnabled)
        }
    }
}