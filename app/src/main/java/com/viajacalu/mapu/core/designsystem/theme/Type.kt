package com.viajacalu.mapu.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.viajacalu.mapu.R

// Both fonts are variable, so each weight is an instance of the same file
@OptIn(ExperimentalTextApi::class)
private fun variableFont(resId: Int, weight: FontWeight) = Font(
    resId = resId,
    weight = weight,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight))
)

// Serif for display, headline and large titles
val Fraunces = FontFamily(
    variableFont(R.font.fraunces, FontWeight.Normal),
    variableFont(R.font.fraunces, FontWeight.SemiBold)
)

// Sans-serif for everything else
val Manrope = FontFamily(
    variableFont(R.font.manrope, FontWeight.Normal),
    variableFont(R.font.manrope, FontWeight.Medium),
    variableFont(R.font.manrope, FontWeight.SemiBold),
    variableFont(R.font.manrope, FontWeight.Bold)
)

private val Baseline = Typography()

// Keeps the Material 3 type scale sizes and swaps in the brand fonts
val Typography = Typography(
    displayLarge = Baseline.displayLarge.copy(fontFamily = Fraunces, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.5).sp),
    displayMedium = Baseline.displayMedium.copy(fontFamily = Fraunces, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.5).sp),
    displaySmall = Baseline.displaySmall.copy(fontFamily = Fraunces, fontWeight = FontWeight.SemiBold),
    headlineLarge = Baseline.headlineLarge.copy(fontFamily = Fraunces, fontWeight = FontWeight.SemiBold),
    headlineMedium = Baseline.headlineMedium.copy(fontFamily = Fraunces, fontWeight = FontWeight.SemiBold),
    headlineSmall = Baseline.headlineSmall.copy(fontFamily = Fraunces, fontWeight = FontWeight.SemiBold),
    titleLarge = Baseline.titleLarge.copy(fontFamily = Fraunces, fontWeight = FontWeight.SemiBold),
    titleMedium = Baseline.titleMedium.copy(fontFamily = Manrope, fontWeight = FontWeight.SemiBold),
    titleSmall = Baseline.titleSmall.copy(fontFamily = Manrope, fontWeight = FontWeight.SemiBold),
    bodyLarge = Baseline.bodyLarge.copy(fontFamily = Manrope, letterSpacing = 0.15.sp),
    bodyMedium = Baseline.bodyMedium.copy(fontFamily = Manrope, letterSpacing = 0.15.sp),
    bodySmall = Baseline.bodySmall.copy(fontFamily = Manrope, letterSpacing = 0.2.sp),
    labelLarge = Baseline.labelLarge.copy(fontFamily = Manrope, fontWeight = FontWeight.SemiBold),
    labelMedium = Baseline.labelMedium.copy(fontFamily = Manrope, fontWeight = FontWeight.SemiBold),
    labelSmall = Baseline.labelSmall.copy(fontFamily = Manrope, fontWeight = FontWeight.SemiBold)
)
