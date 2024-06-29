/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
fun WidgetCard(
    modifier: Modifier = Modifier,
    heading: String? = null,
    contents: @Composable () -> Unit,
    footer: @Composable (() -> Unit)? = null,
) {
    val dimension = LocalDensity.current.getDimension()

    Card(
        modifier = modifier
            .sizeIn(
                minWidth = dimension.windowWidthMedium / 2,
                maxWidth = dimension.windowWidthCompact,
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = dimension.grid_2),
            verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
        ) {
            heading?.let { headingText ->
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    text = headingText,
                )
            }

            Spacer(modifier = Modifier.weight(weight = 1f))

            contents()

            Spacer(modifier = Modifier.weight(weight = 1f))

            footer?.let { it() }
        }
    }
}
