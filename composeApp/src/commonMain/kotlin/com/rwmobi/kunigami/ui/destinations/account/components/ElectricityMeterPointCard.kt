/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.domain.model.ElectricityMeterPoint
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.ui.destinations.account.AccountScreenLayout
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
internal fun ElectricityMeterPointCard(
    selectedMpan: String?,
    selectedMeterSerialNumber: String?,
    meterPoint: ElectricityMeterPoint,
    tariff: Tariff?,
    requestedLayout: AccountScreenLayout,
) {
    val dimension = LocalDensity.current.getDimension()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimension.grid_2),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimension.grid_2),
            verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
        ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = "MPAN: ${meterPoint.mpan}",
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimension.grid_1),
                color = MaterialTheme.colorScheme.inverseSurface,
            )

            if (tariff == null) {
                Text("Could not retrieve your traiff. retry?")
            } else {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimension.grid_2),
                ) {
                    if (requestedLayout is AccountScreenLayout.Compact) {
                        TariffLayoutCompact(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            tariff = tariff,
                            currentAgreement = meterPoint.currentAgreement,
                        )
                    } else {
                        TariffLayoutWide(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            tariff = tariff,
                            currentAgreement = meterPoint.currentAgreement,
                        )
                    }
                }
            }

            val meterSerialNumberTextStyle = if (requestedLayout is AccountScreenLayout.Compact) {
                MaterialTheme.typography.labelMedium
            } else {
                MaterialTheme.typography.titleMedium
            }

            meterPoint.meterSerialNumbers.forEach { meterSerialNumber ->
                MeterSerialNumberEntry(
                    selectedMpan = selectedMpan,
                    selectedMeterSerialNumber = selectedMeterSerialNumber,
                    meterSerialNumber = meterSerialNumber,
                    meterPoint = meterPoint,
                    meterSerialNumberTextStyle = meterSerialNumberTextStyle,
                )

                Spacer(modifier = Modifier.size(size = dimension.grid_1))
            }
        }
    }
}
