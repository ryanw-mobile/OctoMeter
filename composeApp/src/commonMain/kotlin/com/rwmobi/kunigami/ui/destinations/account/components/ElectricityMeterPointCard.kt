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

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import com.rwmobi.kunigami.ui.components.IconTextButton
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.destinations.account.AccountScreenLayoutStyle
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_error_null_tariff
import kunigami.composeapp.generated.resources.account_mpan
import kunigami.composeapp.generated.resources.reload
import kunigami.composeapp.generated.resources.retry
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ElectricityMeterPointCard(
    modifier: Modifier = Modifier,
    selectedMpan: String?,
    selectedMeterSerialNumber: String?,
    meterPoint: ElectricityMeterPoint,
    requestedLayout: AccountScreenLayoutStyle,
    onReloadTariff: () -> Unit,
    onMeterSerialNumberSelected: (mpan: String, meterSerialNumber: String) -> Unit,
) {
    val dimension = getScreenSizeInfo().getDimension()

    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimension.grid_2),
            verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .padding(
                        horizontal = dimension.grid_2,
                        vertical = dimension.grid_0_5,
                    ),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                text = stringResource(resource = Res.string.account_mpan, meterPoint.mpan),
            )

            if (meterPoint.agreements.isNotEmpty()) {
                meterPoint.agreements
                    .sortedByDescending { it.period.start }
                    .forEachIndexed { index, agreement ->
                        TariffLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = dimension.grid_2,
                                    end = dimension.grid_2,
                                    bottom = dimension.grid_1,
                                )
                                .wrapContentHeight(),
                            agreement = agreement,
                            showDivider = index < meterPoint.agreements.lastIndex,
                        )
                    }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimension.grid_2,
                            end = dimension.grid_2,
                            bottom = dimension.grid_2,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
                ) {
                    Text(text = stringResource(resource = Res.string.account_error_null_tariff))

                    IconTextButton(
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        icon = painterResource(resource = Res.drawable.reload),
                        text = stringResource(resource = Res.string.retry),
                        onClick = onReloadTariff,
                    )
                }
            }

            val meterSerialNumberTextStyle = if (requestedLayout is AccountScreenLayoutStyle.Compact) {
                MaterialTheme.typography.labelMedium
            } else {
                MaterialTheme.typography.titleMedium
            }

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = dimension.grid_2),
                verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
            ) {
                meterPoint.meters.forEach { meter ->
                    MeterSerialNumberEntry(
                        selectedMpan = selectedMpan,
                        selectedMeterSerialNumber = selectedMeterSerialNumber,
                        mpan = meterPoint.mpan,
                        meter = meter,
                        meterSerialNumberTextStyle = meterSerialNumberTextStyle,
                        onMeterSerialNumberSelected = { onMeterSerialNumberSelected(meterPoint.mpan, meter.serialNumber) },
                    )
                }
            }
        }
    }
}
