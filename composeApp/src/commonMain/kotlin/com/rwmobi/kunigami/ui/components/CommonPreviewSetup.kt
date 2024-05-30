/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.Dimension
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
internal fun CommonPreviewSetup(
    modifier: Modifier = Modifier,
    content: @Composable (dimension: Dimension) -> Unit = {},
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val size = 16.dp.toPx() // Size of the checker square
                val lightColor = Color(0xFFD3D3D3) // Light grey color
                val darkColor = Color(0xFFA9A9A9) // Dark grey color

                for (i in 0 until (size.toInt() * 2)) {
                    for (j in 0 until (size.toInt() * 2)) {
                        val color = if ((i + j) % 2 == 0) lightColor else darkColor
                        drawRect(
                            color = color,
                            topLeft = androidx.compose.ui.geometry.Offset(x = i * size, y = j * size),
                            size = androidx.compose.ui.geometry.Size(size, size),
                            style = Fill,
                        )
                    }
                }
            }
            .verticalScroll(
                state = rememberScrollState(),
            ),
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_4),
    ) {
        AppTheme(
            useDarkTheme = false,
        ) {
            Surface {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
                ) {
                    content(dimension)
                }
            }
        }

        AppTheme(
            useDarkTheme = true,
        ) {
            Surface {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
                ) {
                    content(dimension)
                }
            }
        }
    }
}
