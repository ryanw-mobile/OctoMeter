/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
internal fun DashboardWidgetCard(
    modifier: Modifier = Modifier,
    countDownText: String?,
    targetPercentage: Float,
    colorPalette: List<Color>,
) {
    val dimension = LocalDensity.current.getDimension()
    Card(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = dimension.grid_2),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            DashboardWidget(
                modifier = Modifier.aspectRatio(ratio = 2f),
                colorPalette = colorPalette,
                percentage = targetPercentage,
                countDownText = countDownText,
            )
        }
    }
}
