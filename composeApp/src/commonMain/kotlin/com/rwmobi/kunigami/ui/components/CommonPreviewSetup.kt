/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
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
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.Dimension
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
internal fun CommonPreviewSetup(
    modifier: Modifier = Modifier,
    content: @Composable (dimension: Dimension) -> Unit = {},
) {
    val dimension = getScreenSizeInfo().getDimension()

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
