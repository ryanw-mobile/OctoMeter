/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
fun IndicatorTextValueGridItem(
    modifier: Modifier = Modifier,
    indicatorColor: Color,
    label: String,
    value: String,
) {
    val dimension = LocalDensity.current.getDimension()
    Row(
        modifier = modifier.fillMaxWidth()
            .drawBehind {
                val width = dimension.grid_1.toPx()
                drawRect(
                    color = indicatorColor,
                    size = Size(width, size.height),
                )
            }
            .padding(vertical = dimension.grid_0_25),
        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(width = dimension.grid_1))

        Text(
            modifier = Modifier.wrapContentWidth(),
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.bodyMedium,
            text = label,
        )

        val dottedLineColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.16f)
        Box(
            modifier = Modifier
                .weight(1.0f)
                .height(intrinsicSize = IntrinsicSize.Max)
                .drawBehind {
                    val lineHeight = size.height
                    val dotRadius = 1.dp.toPx()
                    val dotGap = 1.dp.toPx()

                    var currentX = 0f
                    while (currentX < size.width) {
                        drawCircle(
                            color = dottedLineColor,
                            radius = dotRadius,
                            center = Offset(currentX, lineHeight / 2),
                        )
                        currentX += dotRadius * 2 + dotGap
                    }
                },
        )

        Text(
            modifier = Modifier.wrapContentWidth(),
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            text = value,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            IndicatorTextValueGridItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                indicatorColor = Color.Red,
                label = "03:30",
                value = "0.05",
            )
        }
    }
}
