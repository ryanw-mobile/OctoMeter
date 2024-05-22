/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.chevron_left_circle
import kunigami.composeapp.generated.resources.chevron_right_circle
import kunigami.composeapp.generated.resources.content_description_previous_period
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TitleNavigationBar(
    modifier: Modifier = Modifier,
    title: String,
    onSelectPresentationStyle: () -> Unit,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit,
    canNavigateForward: Boolean,
    onNavigateForward: () -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(size = dimension.minTouchTarget)
                .clickable(
                    enabled = canNavigateBack,
                    onClick = onNavigateBack,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (canNavigateBack) {
                Icon(
                    modifier = Modifier.padding(all = dimension.grid_1),
                    tint = MaterialTheme.colorScheme.onSecondary,
                    painter = painterResource(resource = Res.drawable.chevron_left_circle),
                    contentDescription = stringResource(resource = Res.string.content_description_previous_period),
                )
            }
        }

        Button(
            modifier = Modifier
                .padding(vertical = dimension.grid_1)
                .weight(weight = 1f),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                    alpha = 0.32f,
                ),
            ),
            onClick = onSelectPresentationStyle,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                text = title,
            )
        }

        Box(
            modifier = Modifier
                .size(size = dimension.minTouchTarget)
                .clickable(
                    enabled = canNavigateForward,
                    onClick = onNavigateForward,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (canNavigateForward) {
                Icon(
                    modifier = Modifier.padding(all = dimension.grid_1),
                    tint = MaterialTheme.colorScheme.onSecondary,
                    painter = painterResource(resource = Res.drawable.chevron_right_circle),
                    contentDescription = stringResource(resource = Res.string.content_description_previous_period),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        Surface {
            TitleNavigationBar(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.secondary)
                    .fillMaxWidth()
                    .height(height = 64.dp),
                title = "Sample title",
                onSelectPresentationStyle = {},
                canNavigateBack = true,
                onNavigateBack = {},
                canNavigateForward = true,
                onNavigateForward = {},
            )
        }
    }
}
