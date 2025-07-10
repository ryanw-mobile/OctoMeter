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
import com.rwmobi.kunigami.domain.model.account.ElectricityMeter
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.theme.AppTheme
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_meter_serial
import kunigami.composeapp.generated.resources.dashboard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

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
    CompositionLocalProvider(
        LocalDensity provides Density(currentDensity.density, fontScale = 1f),
    ) {
        val isMeterSelected = (mpan == selectedMpan) && (meter.serialNumber == selectedMeterSerialNumber)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = AppTheme.shapes.small)
                .background(color = AppTheme.colorScheme.surfaceContainerLow)
                .heightIn(min = AppTheme.dimens.minTouchTarget)
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
                    horizontal = AppTheme.dimens.grid_1,
                    vertical = AppTheme.dimens.grid_0_5,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = AppTheme.dimens.grid_1),
        ) {
            Image(
                modifier = Modifier
                    .size(size = AppTheme.dimens.grid_3),
                colorFilter = ColorFilter.tint(color = AppTheme.colorScheme.onSurfaceVariant),
                painter = painterResource(resource = Res.drawable.dashboard),
                contentDescription = null,
            )

            with(meter) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis,
                        style = meterSerialNumberTextStyle,
                        color = AppTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        text = stringResource(Res.string.account_meter_serial, serialNumber),
                    )

                    makeAndType?.let {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            overflow = TextOverflow.Ellipsis,
                            style = AppTheme.typography.bodySmall,
                            maxLines = 1,
                            text = it,
                        )
                    }

                    if (readingSource != null && readAt != null && value != null) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            style = AppTheme.typography.bodySmall,
                            text = "$readingSource: ${value.toInt()} (${readAt.getLocalDateString()})",
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
                deviceId = "FF-FF-FF-FF-FF-FF-FF-FF",
                readingSource = "Smart reading",
                readAt = Instant.parse("2024-07-21T00:00:00+00:00"),
                value = 2480.10,
            ),
            meterSerialNumberTextStyle = AppTheme.typography.titleMedium,
            onMeterSerialNumberSelected = {},
        )
    }
}
