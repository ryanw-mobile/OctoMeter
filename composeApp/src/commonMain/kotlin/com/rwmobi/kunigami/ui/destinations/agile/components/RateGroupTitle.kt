/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.theme.AppTheme
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
            style = AppTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = title,
        )

        Text(
            modifier = Modifier.wrapContentSize(),
            style = AppTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            text = stringResource(resource = Res.string.agile_vat_unit_rate),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        RateGroupTitle(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = AppTheme.dimens.grid_2,
                    horizontal = AppTheme.dimens.grid_4,
                ),
            title = "Sample title",
        )
    }
}
