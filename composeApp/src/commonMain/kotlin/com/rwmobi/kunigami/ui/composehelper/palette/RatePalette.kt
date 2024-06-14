/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.composehelper.palette

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

/**
 * Implemented as an object because we want it a singleton and have consistent color resolution across the app.
 * Being a singleton can help avoid this being recreated (thus spectrum regenerated) during recompositions.
 * Callers can use this without holding the object in a variable, works much like an extension function.
 */
object RatePalette {
    private val positiveSpectrum: List<Color> = generateGYRHueSpectrum(
        saturation = 0.6f,
        lightness = 0.6f,
    )
    private val negativeSpectrum: List<Color> = generateFreezingBlueSpectrum(
        saturation = 0.6f,
        lightness = 0.6f,
    )

    fun getPositiveSpectrum() = positiveSpectrum
    fun getNegativeSpectrum() = negativeSpectrum

    /***
     * Returns a Color that represents the ratio within the range of spectrum.
     * Positive (>=0) and negative values will be treated separately.
     * Therefore, the given the same max in the range, same positive value will always
     * resolves into the same color regardless the negative min in the range (if any).
     */
    fun lookupColorFromRange(value: Double, range: ClosedFloatingPointRange<Double>): Color {
        val effectiveValue = value.coerceIn(range)
        val isPositive = effectiveValue >= 0
        val effectiveSpectrum = if (isPositive) positiveSpectrum else negativeSpectrum
        val effectiveRange = if (isPositive) 0.0..range.endInclusive else range.start..0.0
        val rangeSpan = effectiveRange.endInclusive - effectiveRange.start

        // Avoid division by zero if the range span is zero
        val effectiveColorIndex = if (rangeSpan != 0.0) {
            ((abs(effectiveValue - effectiveRange.start) / rangeSpan) * (effectiveSpectrum.size - 1)).toInt()
        } else {
            0
        }

        return effectiveSpectrum[effectiveColorIndex.coerceIn(0, effectiveSpectrum.size - 1)]
    }

    /**
     * percentage: if negative, it will return the color in negativeSpectrum
     */
    fun lookupColorFromPercentage(percentage: Float): Color {
        val effectivePercentage = percentage.coerceIn(-1f..1f)
        val isPositive = effectivePercentage >= 0
        val effectiveSpectrum = if (isPositive) positiveSpectrum else negativeSpectrum

        val index = (abs(effectivePercentage) * (effectiveSpectrum.size - 1)).toInt()

        return effectiveSpectrum[index.coerceIn(0, effectiveSpectrum.size - 1)]
    }
}
