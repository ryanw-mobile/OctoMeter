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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_meter_select
import kunigami.composeapp.generated.resources.account_meter_selected
import kunigami.composeapp.generated.resources.account_meter_serial
import kunigami.composeapp.generated.resources.dashboard
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun MeterSerialNumberEntry(
    selectedMpan: String?,
    selectedMeterSerialNumber: String?,
    mpan: String,
    meterSerialNumber: String,
    meterSerialNumberTextStyle: TextStyle,
    onSelectMeterSerialNumber: () -> Unit,
) {
    val currentDensity = LocalDensity.current
    val dimension = currentDensity.getDimension()
    CompositionLocalProvider(
        LocalDensity provides Density(currentDensity.density, fontScale = 1f),
    ) {
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
                text = stringResource(Res.string.account_meter_serial, meterSerialNumber),
            )

            if (mpan == selectedMpan && meterSerialNumber == selectedMeterSerialNumber) {
                Button(
                    modifier = Modifier
                        .padding(
                            horizontal = dimension.grid_2,
                            vertical = dimension.grid_1,
                        ),

                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    ),
                    enabled = false,
                    onClick = { },
                ) {
                    Text(
                        modifier = Modifier.width(width = 64.dp),
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        text = stringResource(Res.string.account_meter_selected),
                    )
                }
            } else {
                OutlinedButton(
                    modifier = Modifier
                        .padding(
                            horizontal = dimension.grid_2,
                            vertical = dimension.grid_1,
                        ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.tertiaryContainer,
                        containerColor = Color.Transparent,
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = SolidColor(value = MaterialTheme.colorScheme.tertiaryContainer),
                    ),
                    onClick = { onSelectMeterSerialNumber() },
                ) {
                    Text(
                        modifier = Modifier.width(width = 64.dp),
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        text = stringResource(Res.string.account_meter_select),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface(modifier = Modifier.padding(all = 8.dp)) {
            MeterSerialNumberEntry(
                selectedMpan = "1200000345678",
                selectedMeterSerialNumber = "11A1234567",
                mpan = "1200000345678",
                meterSerialNumber = "11A1234567",
                meterSerialNumberTextStyle = MaterialTheme.typography.titleMedium,
                onSelectMeterSerialNumber = {},
            )
        }
    }
}
