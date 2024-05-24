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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_demo_introduction
import kunigami.composeapp.generated.resources.blackboard
import kunigami.composeapp.generated.resources.key
import kunigami.composeapp.generated.resources.provide_api_key
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoModeCTAWide(
    modifier: Modifier,
    description: String,
    ctaButtonLabel: String,
    onCtaButtonClicked: () -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
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
    AppTheme {
        Surface(modifier = Modifier.padding(all = 16.dp)) {
            DemoModeCTAWide(
                modifier = Modifier.fillMaxWidth(),
                description = stringResource(resource = Res.string.agile_demo_introduction),
                ctaButtonLabel = stringResource(resource = Res.string.provide_api_key),
                onCtaButtonClicked = {},
            )
        }
    }
}