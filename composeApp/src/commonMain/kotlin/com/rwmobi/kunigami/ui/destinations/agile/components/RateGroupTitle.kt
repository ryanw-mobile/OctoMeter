/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_vat_unit_rate
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RateGroupTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = title,
        )

        Text(
            modifier = Modifier.wrapContentSize(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            text = stringResource(resource = Res.string.agile_vat_unit_rate),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup { dimension ->
        RateGroupTitle(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimension.grid_2,
                    horizontal = dimension.grid_4,
                ),
            title = "Sample title",
        )
    }
}
