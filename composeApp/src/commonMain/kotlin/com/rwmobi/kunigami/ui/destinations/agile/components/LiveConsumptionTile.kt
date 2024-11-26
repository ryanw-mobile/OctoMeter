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

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.rwmobi.kunigami.domain.model.consumption.LiveConsumption
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Clock
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_live_consumption
import kunigami.composeapp.generated.resources.w
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LiveConsumptionTile(
    modifier: Modifier = Modifier,
    liveConsumption: LiveConsumption,
) {
    val dimension = getScreenSizeInfo().getDimension()
    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(dimension.grid_2),
        verticalArrangement = Arrangement.Top,
    ) {
        val animatedVatInclusivePrice by animateIntAsState(
            targetValue = liveConsumption.demand,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimension.grid_1),
        ) {
            Text(
                modifier = Modifier.wrapContentSize(),
                maxLines = 1,
                overflow = TextOverflow.Clip,
                style = MaterialTheme.typography.displayMedium,
                text = animatedVatInclusivePrice.toString(),
            )

            Text(
                modifier = Modifier.wrapContentSize(),
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                text = stringResource(resource = Res.string.w),
            )
        }

        Spacer(modifier.weight(1f))

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(resource = Res.string.agile_live_consumption),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup { dimension ->
        LiveConsumptionTile(
            modifier = Modifier
                .width(dimension.widgetWidthFull)
                .height(dimension.widgetHeight),
            LiveConsumption(
                readAt = Clock.System.now(),
                demand = 1560,
            ),
        )
    }
}
