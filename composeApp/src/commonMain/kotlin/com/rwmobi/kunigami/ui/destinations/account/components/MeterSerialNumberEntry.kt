/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.model.ElectricityMeterPoint
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.dashboard
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun MeterSerialNumberEntry(
    selectedMpan: String?,
    selectedMeterSerialNumber: String?,
    meterSerialNumber: String,
    meterPoint: ElectricityMeterPoint,
    meterSerialNumberTextStyle: TextStyle,
) {
    val dimension = LocalDensity.current.getDimension()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .requiredHeight(height = dimension.minTouchTarget),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .padding(horizontal = dimension.grid_2)
                .size(size = dimension.grid_3),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurfaceVariant),
            painter = painterResource(resource = Res.drawable.dashboard),
            contentDescription = null,
        )

        Text(
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis,
            style = meterSerialNumberTextStyle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            text = "Serial: $meterSerialNumber",
        )

        if (meterPoint.mpan == selectedMpan && meterSerialNumber == selectedMeterSerialNumber) {
            Text(
                modifier = Modifier.padding(horizontal = dimension.grid_2)
                    .clip(shape = MaterialTheme.shapes.large)
                    .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(
                        horizontal = dimension.grid_2,
                        vertical = dimension.grid_1,
                    ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                text = "SELECTED",
            )
        } else {
            Button(
                onClick = { },
                modifier = Modifier
                    .padding(horizontal = dimension.grid_2)
                    .clip(shape = MaterialTheme.shapes.large)
                    .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(horizontal = dimension.grid_2, vertical = dimension.grid_1),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                ),
                shape = MaterialTheme.shapes.large,
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
            ) {
                Text(
                    text = "SELECT",
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}
