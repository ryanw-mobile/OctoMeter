/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.AppTheme

@PreviewLightDark
@PreviewFontScale
@Composable
private fun Preview() {
    AppTheme {
        Surface(
            modifier = Modifier.padding(all = 16.dp),
        ) {
            IndicatorTextValueGridItem(
                indicatorColor = Color.Red,
                label = "Sample label",
                value = "12.22",
            )
        }
    }
}
