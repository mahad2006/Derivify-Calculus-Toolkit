package com.codewithmahad.derivativecalculator.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.codewithmahad.derivativecalculator.data.Difficulty // NEW import
import com.codewithmahad.derivativecalculator.data.QuizData
import com.codewithmahad.derivativecalculator.data.QuizQuestion
import kotlin.random.Random // NEW import

enum class QuizState {
    NOT_STARTED,
    IN_PROGRESS,
    FINISHED
}

class QuizViewModel : ViewModel() {

    // This is no longer loaded at the start. It's loaded when a difficulty is chosen.
    private var questionsList: List<QuizQuestion> = emptyList()

    // This will be set to 10 when the quiz starts
    var totalQuestions by mutableIntStateOf(0)
        private set

    private var questionIndex by mutableIntStateOf(0)

    var currentQuestion by mutableStateOf<QuizQuestion?>(null)
        private set

    // NEW: We need to store the shuffled options for the current question
    var shuffledOptions by mutableStateOf<List<String>>(emptyList())
        private set

    var score by mutableIntStateOf(0)
        private set

    var quizState by mutableStateOf(QuizState.NOT_STARTED)
        private set

    var selectedOption by mutableStateOf<String?>(null)
        private set

    var isAnswerCorrect by mutableStateOf<Boolean?>(null)
        private set

    // UPDATED: startQuiz now takes a difficulty
    fun startQuiz(difficulty: Difficulty) {
        // Get 10 random questions for the chosen difficulty
        questionsList = QuizData.getQuestions(difficulty, 10)
        totalQuestions = questionsList.size
        score = 0
        questionIndex = 0
        quizState = QuizState.IN_PROGRESS
        loadQuestion()
    }

    // NEW: Helper function to load the current question and shuffle its options
    private fun loadQuestion() {
        if (questionIndex < questionsList.size) {
            currentQuestion = questionsList[questionIndex]
            // Shuffle the options for the new question
            shuffledOptions = currentQuestion!!.options.shuffled(Random(System.nanoTime()))
            selectedOption = null
            isAnswerCorrect = null
        } else {
            quizState = QuizState.FINISHED
        }
    }

    fun onAnswerSelected(option: String) {
        if (selectedOption == null) { // Prevent changing answer
            selectedOption = option
            isAnswerCorrect = (option == currentQuestion?.correctAnswer)
            if (isAnswerCorrect == true) {
                score++
            }
        }
    }

    fun onNextClicked() {
        // Use questionsList.size instead of allQuestions.size
        if (questionIndex < questionsList.size - 1) {
            questionIndex++
            loadQuestion() // Use the helper function
        } else {
            quizState = QuizState.FINISHED
        }
    }

    // NEW: A function to reset the quiz and go back to the difficulty screen
    fun resetQuiz() {
        quizState = QuizState.NOT_STARTED
        questionIndex = 0
        score = 0
        selectedOption = null
        isAnswerCorrect = null
        currentQuestion = null
        questionsList = emptyList()
    }
}