/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rwmobi.kunigami.domain.extensions.getNextHalfHourCountdownMillis
import com.rwmobi.kunigami.ui.model.rate.RateGroupedCells
import com.rwmobi.kunigami.ui.model.rate.findActiveRate
import com.rwmobi.kunigami.ui.model.rate.getRateTrend
import io.github.koalaplot.core.util.toString
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

@Composable
internal fun CountDownWidget(
    modifier: Modifier,
    colorPalette: List<Color>,
    rateRange: ClosedFloatingPointRange<Double>,
    rateGroupedCells: List<RateGroupedCells>,
) {
    var activeRate by remember { mutableStateOf(rateGroupedCells.findActiveRate(pointOfReference = Clock.System.now())) }
    var rateTrend by remember { mutableStateOf(rateGroupedCells.getRateTrend(activeRate = activeRate)) }
    var expireMillis by remember { mutableStateOf(Clock.System.now().getNextHalfHourCountdownMillis()) }
    var expireMinutes by remember { mutableStateOf(expireMillis / 60_000) }
    var expireSeconds by remember { mutableStateOf((expireMillis / 1_000) % 60) }

    val targetPercentage = ((activeRate?.vatInclusivePrice ?: 0.0) / rateRange.endInclusive).toFloat()
    val animatedPercentage by animateFloatAsState(
        targetValue = targetPercentage,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    )

    CountdownDashboard(
        modifier = modifier,
        colorPalette = colorPalette,
        animatedPercentage = animatedPercentage,
        expireMinutes = expireMinutes,
        expireSeconds = expireSeconds,
        showCountdown = activeRate?.validTo != null,
        vatInclusivePrice = activeRate?.vatInclusivePrice?.toString(precision = 2),
        rateTrend = rateTrend,
    )

    LaunchedEffect(true) {
        while (true) {
            with(activeRate) {
                val isActiveRateExpired =
                    (this == null) ||
                        (validTo?.compareTo(Clock.System.now()) ?: 1) <= 0

                if (isActiveRateExpired) {
                    activeRate =
                        rateGroupedCells.findActiveRate(pointOfReference = Clock.System.now())
                    rateTrend = rateGroupedCells.getRateTrend(activeRate = activeRate)
                }

                expireMillis = activeRate?.validTo?.let {
                    (it - Clock.System.now()).inWholeMilliseconds
                } ?: Clock.System.now().getNextHalfHourCountdownMillis()

                expireMinutes = expireMillis / 60_000
                expireSeconds = (expireMillis / 1_000) % 60
            }

            delay(1_000L)
        }
    }
}
