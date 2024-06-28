/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rwmobi.kunigami.ui.components.WidgetCard

@Composable
internal fun RateGaugeCountdownCard(
    modifier: Modifier = Modifier,
    countDownText: String?,
    targetPercentage: Float,
    colorPalette: List<Color>,
) {
    WidgetCard(
        modifier = modifier,
        contents = {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                DashboardWidget(
                    modifier = Modifier,
                    colorPalette = colorPalette,
                    percentage = targetPercentage,
                    countDownText = countDownText,
                )
            }
        },
    )
}
