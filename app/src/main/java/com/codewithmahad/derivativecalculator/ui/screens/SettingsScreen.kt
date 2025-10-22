package com.codewithmahad.derivativecalculator.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codewithmahad.derivativecalculator.HistoryAutoClear
import com.codewithmahad.derivativecalculator.ThemeOption
import com.codewithmahad.derivativecalculator.ui.components.WhatsNewDialog
import com.codewithmahad.derivativecalculator.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel(),
    contentPadding: PaddingValues
) {
    val currentTheme by settingsViewModel.themeOption.collectAsState()
    val context = LocalContext.current
    val hapticsEnabled by settingsViewModel.hapticFeedbackEnabled.collectAsState()
    val keepScreenOn by settingsViewModel.keepScreenOn.collectAsState()
    val precision by settingsViewModel.precision.collectAsState()
    val historyAutoClear by settingsViewModel.historyAutoClear.collectAsState()
    var showWhatsNewDialog by remember { mutableStateOf(false) }
    var showHistoryClearMenu by remember { mutableStateOf(false) }

    if (showWhatsNewDialog) {
        WhatsNewDialog { showWhatsNewDialog = false }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // --- Section 1: Appearance ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Appearance", style = MaterialTheme.typography.titleLarge)
                Card {
                    Column {
                        ThemeSelectorRow("Light", currentTheme == ThemeOption.LIGHT) {
                            settingsViewModel.setThemeOption(ThemeOption.LIGHT)
                        }
                        Divider()
                        ThemeSelectorRow("Dark", currentTheme == ThemeOption.DARK) {
                            settingsViewModel.setThemeOption(ThemeOption.DARK)
                        }
                        Divider()
                        ThemeSelectorRow("System Default", currentTheme == ThemeOption.SYSTEM) {
                            settingsViewModel.setThemeOption(ThemeOption.SYSTEM)
                        }
                    }
                }
            }
        }

        // --- Section 2: General ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("General", style = MaterialTheme.typography.titleLarge)
                Card {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Keep Screen On", style = MaterialTheme.typography.bodyLarge)
                        Switch(
                            checked = keepScreenOn,
                            onCheckedChange = { settingsViewModel.setKeepScreenOn(it) }
                        )
                    }
                    Divider()
                    // Precision
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Result Precision", style = MaterialTheme.typography.bodyLarge)
                            Text(
                                "$precision decimal places",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Slider(
                            value = precision.toFloat(),
                            onValueChange = { settingsViewModel.setPrecision(it.toInt()) },
                            valueRange = 1f..10f,
                            steps = 8
                        )
                    }
                }
            }
        }

        // --- Section 3: History ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("History", style = MaterialTheme.typography.titleLarge)
                Card {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Auto-clear history", style = MaterialTheme.typography.bodyLarge)
                        Box {
                            IconButton(onClick = { showHistoryClearMenu = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More options")
                            }
                            DropdownMenu(
                                expanded = showHistoryClearMenu,
                                onDismissRequest = { showHistoryClearMenu = false }
                            ) {
                                HistoryAutoClear.values().forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option.name.replace("_", " ").lowercase().replaceFirstChar { it.titlecase() }) },
                                        onClick = {
                                            settingsViewModel.setHistoryAutoClear(option)
                                            showHistoryClearMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        "Current setting: ${historyAutoClear.name.replace("_", " ").lowercase().replaceFirstChar { it.titlecase() }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                    )
                }
            }
        }

        // --- Section 4: Feedback ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Feedback", style = MaterialTheme.typography.titleLarge)
                Card {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Haptic Feedback", style = MaterialTheme.typography.bodyLarge)
                        Switch(
                            checked = hapticsEnabled,
                            onCheckedChange = { isEnabled ->
                                settingsViewModel.setHapticFeedback(isEnabled)
                            }
                        )
                    }
                }
            }
        }

        // --- Section 5: More ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("More", style = MaterialTheme.typography.titleLarge)
                Card {
                    Column {
                        SettingsClickableRow(icon = Icons.Default.Star, text = "Rate this app") {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}"))
                            context.startActivity(intent)
                        }
                        Divider()
                        SettingsClickableRow(icon = Icons.Default.Share, text = "Share app") {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Check out this awesome Calculus app! https://play.google.com/store/apps/details?id=${context.packageName}")
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        }
                        Divider()
                        SettingsClickableRow(icon = Icons.Default.BugReport, text = "Report a bug") {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:codewithmahad@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Derivify App Feedback")
                            }
                            context.startActivity(Intent.createChooser(intent, "Send Feedback"))
                        }
                    }
                }
            }
        }

        // --- Section 6: About ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("About", style = MaterialTheme.typography.titleLarge)
                Card {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("App Version", style = MaterialTheme.typography.bodyLarge)
                            Text("1.0.0", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Divider()
                        SettingsClickableRow(icon = Icons.Default.Info, text = "What's New") {
                            showWhatsNewDialog = true
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsClickableRow(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ThemeSelectorRow(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "$text is selected",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}