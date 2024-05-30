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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_demo_introduction
import kunigami.composeapp.generated.resources.blackboard
import kunigami.composeapp.generated.resources.key
import kunigami.composeapp.generated.resources.provide_api_key
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DemoModeCtaAdaptive(
    modifier: Modifier,
    description: String,
    ctaButtonLabel: String,
    onCtaButtonClicked: () -> Unit,
    useWideLayout: Boolean = false,
) {
    if (useWideLayout) {
        DemoModeCTAWide(
            modifier = modifier,
            description = description,
            ctaButtonLabel = ctaButtonLabel,
            onCtaButtonClicked = onCtaButtonClicked,
        )
    } else {
        DemoModeCTACompact(
            modifier = modifier,
            description = description,
            ctaButtonLabel = ctaButtonLabel,
            onCtaButtonClicked = onCtaButtonClicked,
        )
    }
}

@Composable
private fun DemoModeCTACompact(
    modifier: Modifier,
    description: String,
    ctaButtonLabel: String,
    onCtaButtonClicked: () -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(all = dimension.grid_2),
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
        ) {
            Icon(
                modifier = Modifier.size(size = dimension.grid_4),
                painter = painterResource(resource = Res.drawable.blackboard),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null,
            )

            Text(
                modifier = Modifier.weight(weight = 1f),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = description,
            )
        }

        IconTextButton(
            modifier = Modifier.align(alignment = Alignment.End),
            icon = painterResource(resource = Res.drawable.key),
            text = ctaButtonLabel,
            onClick = onCtaButtonClicked,
        )
    }
}

@Composable
private fun DemoModeCTAWide(
    modifier: Modifier,
    description: String,
    ctaButtonLabel: String,
    onCtaButtonClicked: () -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(
                vertical = dimension.grid_1,
                horizontal = dimension.grid_2,
            ),
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
        ) {
            Icon(
                modifier = Modifier.size(size = dimension.grid_4),
                painter = painterResource(resource = Res.drawable.blackboard),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null,
            )

            Text(
                modifier = Modifier.weight(weight = 1f),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = description,
            )

            IconTextButton(
                icon = painterResource(resource = Res.drawable.key),
                text = ctaButtonLabel,
                onClick = onCtaButtonClicked,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        DemoModeCtaAdaptive(
            modifier = Modifier.fillMaxWidth(),
            description = stringResource(resource = Res.string.agile_demo_introduction),
            ctaButtonLabel = stringResource(resource = Res.string.provide_api_key),
            onCtaButtonClicked = {},
            useWideLayout = false,
        )

        DemoModeCtaAdaptive(
            modifier = Modifier.fillMaxWidth(),
            description = stringResource(resource = Res.string.agile_demo_introduction),
            ctaButtonLabel = stringResource(resource = Res.string.provide_api_key),
            onCtaButtonClicked = {},
            useWideLayout = true,
        )
    }
}
