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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_mpan
import kunigami.composeapp.generated.resources.dashboard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun NavigationOptionsBar(
    modifier: Modifier = Modifier,
    selectedMpan: String?,
) {
    val dimension = LocalDensity.current.getDimension()
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
                .fillMaxWidth()
                .height(height = dimension.minListItemHeight)
                .padding(horizontal = dimension.grid_2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            selectedMpan?.let { mpan ->
                Icon(
                    modifier = Modifier
                        .size(size = dimension.grid_4)
                        .padding(end = dimension.grid_1),
                    painter = painterResource(resource = Res.drawable.dashboard),
                    contentDescription = null,
                )
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = stringResource(resource = Res.string.account_mpan, mpan),
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
        )
    }
}
