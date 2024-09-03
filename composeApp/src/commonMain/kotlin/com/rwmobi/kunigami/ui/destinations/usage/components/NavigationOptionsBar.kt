/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.usage.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.dashboard
import kunigami.composeapp.generated.resources.skip_forward
import kunigami.composeapp.generated.resources.usage_latest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun NavigationOptionsBar(
    modifier: Modifier = Modifier,
    selectedMpan: String?,
    onNavigateToLatest: () -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
                .fillMaxWidth()
                .height(height = dimension.minListItemHeight),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            selectedMpan?.let { mpan ->
                Icon(
                    modifier = Modifier
                        .size(size = dimension.grid_4)
                        .padding(start = dimension.grid_2),
                    painter = painterResource(resource = Res.drawable.dashboard),
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier.padding(start = dimension.grid_1),
                    style = MaterialTheme.typography.bodyMedium,
                    text = mpan,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                modifier = Modifier.size(size = dimension.imageButtonSize),
                onClick = onNavigateToLatest,
            ) {
                Icon(
                    modifier = Modifier.padding(
                        horizontal = dimension.grid_1,
                        vertical = dimension.grid_0_5,
                    ),
                    painter = painterResource(resource = Res.drawable.skip_forward),
                    contentDescription = stringResource(resource = Res.string.usage_latest),
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        NavigationOptionsBar(
            modifier = Modifier.fillMaxWidth(),
            selectedMpan = "1200000123456",
            onNavigateToLatest = {},
        )
    }
}
