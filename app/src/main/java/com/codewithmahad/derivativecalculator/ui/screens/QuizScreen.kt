package com.codewithmahad.derivativecalculator.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codewithmahad.derivativecalculator.data.Difficulty // NEW import
import com.codewithmahad.derivativecalculator.ui.components.LatexView
import com.codewithmahad.derivativecalculator.viewmodels.QuizState
import com.codewithmahad.derivativecalculator.viewmodels.QuizViewModel

@Composable
fun QuizScreen(
    contentPadding: PaddingValues,
    quizViewModel: QuizViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (quizViewModel.quizState) {
            // UPDATED: Pass the new lambda to start the quiz
            QuizState.NOT_STARTED -> QuizStartScreen(
                onStartClick = { difficulty ->
                    quizViewModel.startQuiz(difficulty)
                }
            )
            QuizState.IN_PROGRESS -> QuizInProgressScreen(
                viewModel = quizViewModel,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            QuizState.FINISHED -> QuizFinishedScreen(
                score = quizViewModel.score,
                totalQuestions = quizViewModel.totalQuestions,
                // UPDATED: Call the new resetQuiz function
                onPlayAgain = { quizViewModel.resetQuiz() }
            )
        }
    }
}

/**
 * --- COMPLETELY NEW UI ---
 * This screen now shows four buttons for difficulty selection.
 */
@Composable
fun QuizStartScreen(onStartClick: (Difficulty) -> Unit) {
    // Define the traffic-light colors
    val easyColor = Color(0xFF4CAF50) // Green
    val mediumColor = Color(0xFFFF9800) // Orange
    val hardColor = Color(0xFFF44336) // Red
    val nightmareColor = Color(0xFFB71C1C) // Dark Red

    Icon(Icons.Default.Quiz, "Quiz Icon", modifier = Modifier.size(100.dp), tint = MaterialTheme.colorScheme.primary)
    Spacer(modifier = Modifier.height(16.dp))
    Text("Select Difficulty", style = MaterialTheme.typography.headlineSmall)
    Text("Each quiz has 10 random questions.", textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
    Spacer(modifier = Modifier.height(24.dp))

    Column(
        modifier = Modifier.fillMaxWidth(0.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // EASY
        Button(
            onClick = { onStartClick(Difficulty.EASY) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = easyColor, contentColor = Color.White)
        ) {
            Text("Easy")
        }

        // MEDIUM
        Button(
            onClick = { onStartClick(Difficulty.MEDIUM) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = mediumColor, contentColor = Color.White)
        ) {
            Text("Medium")
        }

        // HARD
        Button(
            onClick = { onStartClick(Difficulty.HARD) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = hardColor, contentColor = Color.White)
        ) {
            Text("Hard")
        }

        // NIGHTMARE
        Button(
            onClick = { onStartClick(Difficulty.NIGHTMARE) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = nightmareColor,
                contentColor = Color.White
            )
        ) {
            Text("Nightmare")
        }
    }
}

@Composable
fun QuizInProgressScreen(viewModel: QuizViewModel, modifier: Modifier = Modifier) {
    val question = viewModel.currentQuestion ?: return
    // UPDATED: Get shuffled options from the ViewModel
    val options = viewModel.shuffledOptions

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- PART 1: Fixed Question Area (Top) ---
        // Progress Text
        Text(
            // A bit of logic to show the correct question number
            text = "Question ${viewModel.totalQuestions - (viewModel.totalQuestions - viewModel.score - 1)} / ${viewModel.totalQuestions}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text("What is the derivative of:", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Box(contentAlignment = Alignment.Center) {
            LatexView(latex = question.function)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Category: ${question.rule}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))

        // --- PART 2: Scrollable Options Area (Middle) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // UPDATED: Iterate over the options from the ViewModel
            options.forEach { option ->
                val isSelected = (viewModel.selectedOption == option)

                QuizOptionCard(
                    optionText = option,
                    isSelected = isSelected,
                    isCorrect = (option == question.correctAnswer) && (viewModel.selectedOption != null),
                    isAnswered = viewModel.selectedOption != null,
                    onClick = {
                        if (viewModel.selectedOption == null) {
                            viewModel.onAnswerSelected(option)
                        }
                    }
                )
            }
        }

        // --- PART 3: Fixed Button Area (Bottom) ---
        if (viewModel.selectedOption != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.onNextClicked() },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Show "Finish" on the last question
                Text(if (viewModel.totalQuestions == (viewModel.totalQuestions - viewModel.score)) "Finish" else "Next")
            }
        }
    }
}

@Composable
private fun QuizOptionCard(
    optionText: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isAnswered: Boolean,
    onClick: () -> Unit
) {
    val borderColor: Color
    val backgroundColor: Color

    when {
        isSelected && isCorrect -> {
            borderColor = Color.Green.copy(alpha = 0.8f)
            backgroundColor = Color.Green.copy(alpha = 0.1f)
        }
        isSelected && !isCorrect -> {
            borderColor = Color.Red.copy(alpha = 0.8f)
            backgroundColor = Color.Red.copy(alpha = 0.1f)
        }
        !isSelected && isCorrect && isAnswered -> {
            borderColor = Color.Green.copy(alpha = 0.5f)
            backgroundColor = Color.Green.copy(alpha = 0.05f)
        }
        else -> {
            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, borderColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val selectionBoxColor = if (isSelected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            } else {
                Color.Transparent
            }

            Box(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxHeight()
                    .clickable(
                        enabled = !isAnswered,
                        onClick = onClick
                    )
                    .background(selectionBoxColor)
                    .border(
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(0.75f)
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                LatexView(latex = optionText)
            }
        }
    }
}


@Composable
fun QuizFinishedScreen(score: Int, totalQuestions: Int, onPlayAgain: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Quiz Complete!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Your Score:", style = MaterialTheme.typography.titleLarge)
        Text("$score / $totalQuestions", style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedButton(onClick = onPlayAgain) {
            Icon(Icons.Default.Replay, contentDescription = "Play Again", modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Play Again")
        }
    }
}