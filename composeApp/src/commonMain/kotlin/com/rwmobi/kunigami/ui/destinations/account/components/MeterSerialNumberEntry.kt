/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import com.rwmobi.kunigami.domain.extensions.getLocalDateString
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.account.ElectricityMeter
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Instant
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_meter_serial
import kunigami.composeapp.generated.resources.dashboard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MeterSerialNumberEntry(
    selectedMpan: String?,
    selectedMeterSerialNumber: String?,
    mpan: String,
    meter: ElectricityMeter,
    meterSerialNumberTextStyle: TextStyle,
    onMeterSerialNumberSelected: () -> Unit,
) {
    val currentDensity = LocalDensity.current
    val dimension = currentDensity.getDimension()
    CompositionLocalProvider(
        LocalDensity provides Density(currentDensity.density, fontScale = 1f),
    ) {
        val isMeterSelected = (mpan == selectedMpan) && (meter.serialNumber == selectedMeterSerialNumber)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.small)
                .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
                .heightIn(min = dimension.minTouchTarget)
                .selectable(
                    selected = isMeterSelected,
                    role = Role.RadioButton,
                    onClick = {
                        if (!isMeterSelected) {
                            onMeterSerialNumberSelected()
                        }
                    },
                )
                .padding(
                    horizontal = dimension.grid_1,
                    vertical = dimension.grid_0_5,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
        ) {
            Image(
                modifier = Modifier
                    .size(size = dimension.grid_3),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurfaceVariant),
                painter = painterResource(resource = Res.drawable.dashboard),
                contentDescription = null,
            )

            with(meter) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis,
                        style = meterSerialNumberTextStyle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        text = stringResource(Res.string.account_meter_serial, serialNumber),
                    )

                    makeAndType?.let {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            text = it,
                        )
                    }

                    if (readingSource != null && readAt != null && value != null) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodySmall,
                            text = "$readingSource: ${value.roundToTwoDecimalPlaces()} (${readAt.getLocalDateString()})",
                        )
                    }
                }
            }

            RadioButton(
                selected = isMeterSelected,
                onClick = null,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        MeterSerialNumberEntry(
            selectedMpan = "1200000345678B",
            selectedMeterSerialNumber = "11A1234567",
            mpan = "1200000345678",
            meter = ElectricityMeter(
                serialNumber = "11A1234567",
                makeAndType = "Landis & Gyr E470",
                readingSource = "Smart reading",
                readAt = Instant.parse("2024-07-21T00:00:00+00:00"),
                value = 2480.10,
            ),
            meterSerialNumberTextStyle = MaterialTheme.typography.titleMedium,
            onMeterSerialNumberSelected = {},
        )
    }
}
