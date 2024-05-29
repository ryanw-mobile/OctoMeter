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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.Dimension
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
internal fun CommonPreviewSetup(
    modifier: Modifier = Modifier,
    content: @Composable (dimension: Dimension) -> Unit = {},
) {
    val dimension = LocalDensity.current.getDimension()

    AppTheme {
        Surface {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(
                        state = rememberScrollState(),
                    ),
                verticalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
            ) {
                content(dimension)
            }
        }
    }
}
