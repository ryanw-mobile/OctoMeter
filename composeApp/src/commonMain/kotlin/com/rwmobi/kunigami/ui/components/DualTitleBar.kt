/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
internal fun DualTitleBar(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier.padding(horizontal = dimension.grid_3),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = title,
        )

        subtitle?.let {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(
                    alpha = 0.68f,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = it,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        DualTitleBar(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.secondary)
                .fillMaxWidth()
                .height(height = 64.dp),
            title = "Sample title",
            subtitle = "Sample subtitle",
        )
    }
}
