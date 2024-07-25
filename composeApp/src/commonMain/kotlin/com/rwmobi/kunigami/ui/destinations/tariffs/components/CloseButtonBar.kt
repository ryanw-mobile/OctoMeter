/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup

@Composable
internal fun ButtonTitleBar(
    modifier: Modifier = Modifier
        .background(color = MaterialTheme.colorScheme.secondary)
        .fillMaxWidth()
        .height(height = 56.dp),
    title: String,
    color: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    leftButton: @Composable (() -> Unit)? = null,
    rightButton: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Center),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = title,
        )

        leftButton?.let {
            Box(modifier = Modifier.align(alignment = Alignment.CenterStart)) {
                it()
            }
        }

        rightButton?.let {
            Box(modifier = Modifier.align(alignment = Alignment.CenterEnd)) {
                it()
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup { dimension ->
        ButtonTitleBar(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.secondary)
                .fillMaxWidth()
                .height(height = dimension.minListItemHeight),
            title = "Sample Title",
        )
    }
}
