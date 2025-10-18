package com.codewithmahad.derivativecalculator.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.codewithmahad.derivativecalculator.core.Expression
import com.codewithmahad.derivativecalculator.core.differentiate
import com.codewithmahad.derivativecalculator.core.parse

enum class VectorFieldMode {
    FIELD, DIVERGENCE, CURL
}

data class VectorFieldData(
    val gridPoints: List<Pair<Float, Float>>,
    val vectors: List<Pair<Float, Float>>,
    val divergence: List<Float>,
    val curl: List<Float>
)

class VectorFieldViewModel(application: Application) : AndroidViewModel(application) {

    var pComponentInput by mutableStateOf("y")
    var qComponentInput by mutableStateOf("-x")
    var pComponentError by mutableStateOf<String?>(null)
    var qComponentError by mutableStateOf<String?>(null)

    var vectorFieldData by mutableStateOf<VectorFieldData?>(null)
    var currentMode by mutableStateOf(VectorFieldMode.FIELD)

    fun onPComponentChange(input: String) {
        pComponentInput = input
        pComponentError = null
    }

    fun onQComponentChange(input: String) {
        qComponentInput = input
        qComponentError = null
    }

    fun onVisualizeClicked() {
        pComponentError = if (pComponentInput.isBlank()) "Component cannot be empty" else null
        qComponentError = if (qComponentInput.isBlank()) "Component cannot be empty" else null

        if (pComponentError != null || qComponentError != null) {
            return
        }

        try {
            val pExpr = parse(pComponentInput)
            val qExpr = parse(qComponentInput)

            // Partial derivatives for divergence and curl
            val pDx = pExpr.differentiate('x').finalExpression
            val qDy = qExpr.differentiate('y').finalExpression
            val pDy = pExpr.differentiate('y').finalExpression
            val qDx = qExpr.differentiate('x').finalExpression

            val grid = mutableListOf<Pair<Float, Float>>()
            val vectors = mutableListOf<Pair<Float, Float>>()
            val divergenceValues = mutableListOf<Float>()
            val curlValues = mutableListOf<Float>()

            val range = -10..10
            for (i in range) {
                for (j in range) {
                    val x = i.toFloat()
                    val y = j.toFloat()
                    val values = mapOf('x' to x.toDouble(), 'y' to y.toDouble())
                    
                    grid.add(x to y)

                    val pVal = pExpr.evaluate(values).toFloat()
                    val qVal = qExpr.evaluate(values).toFloat()
                    vectors.add(pVal to qVal)

                    val divergence = pDx.evaluate(values).toFloat() + qDy.evaluate(values).toFloat()
                    divergenceValues.add(divergence)

                    val curl = qDx.evaluate(values).toFloat() - pDy.evaluate(values).toFloat()
                    curlValues.add(curl)
                }
            }
            vectorFieldData = VectorFieldData(grid, vectors, divergenceValues, curlValues)

        } catch (e: Exception) {
            pComponentError = "Invalid expression"
            qComponentError = "Invalid expression"
            e.printStackTrace()
        }
    }
}