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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import com.rwmobi.kunigami.ui.components.IconTextButton
import com.rwmobi.kunigami.ui.destinations.account.AccountScreenLayoutStyle
import com.rwmobi.kunigami.ui.theme.AppTheme
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
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = AppTheme.dimens.grid_2),
            verticalArrangement = Arrangement.spacedBy(space = AppTheme.dimens.grid_1),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = AppTheme.colorScheme.secondaryContainer)
                    .padding(
                        horizontal = AppTheme.dimens.grid_2,
                        vertical = AppTheme.dimens.grid_0_5,
                    ),
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colorScheme.onSecondaryContainer,
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
                                    start = AppTheme.dimens.grid_2,
                                    end = AppTheme.dimens.grid_2,
                                    bottom = AppTheme.dimens.grid_1,
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
                            start = AppTheme.dimens.grid_2,
                            end = AppTheme.dimens.grid_2,
                            bottom = AppTheme.dimens.grid_2,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = AppTheme.dimens.grid_2),
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

            if (meterPoint.meters.isNotEmpty()) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.dimens.grid_2, vertical = AppTheme.dimens.grid_1)
                        .alpha(0.5f),
                )
            }

            val meterSerialNumberTextStyle = if (requestedLayout is AccountScreenLayoutStyle.Compact) {
                AppTheme.typography.labelMedium
            } else {
                AppTheme.typography.titleMedium
            }

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = AppTheme.dimens.grid_2),
                verticalArrangement = Arrangement.spacedBy(space = AppTheme.dimens.grid_1),
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
