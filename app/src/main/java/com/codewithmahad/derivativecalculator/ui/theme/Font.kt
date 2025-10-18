package com.codewithmahad.derivativecalculator.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.codewithmahad.derivativecalculator.R

val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold), // <-- ADD THIS LINE
    Font(R.font.poppins_bold, FontWeight.Bold)
)