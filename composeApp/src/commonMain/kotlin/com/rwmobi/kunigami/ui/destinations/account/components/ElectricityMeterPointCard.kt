/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
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
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.components.IconTextButton
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
    tariffHistory: List<Tariff>,
    requestedLayout: AccountScreenLayoutStyle,
    onReloadTariff: () -> Unit,
    onMeterSerialNumberSelected: (mpan: String, meterSerialNumber: String) -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

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
                        val tariff = tariffHistory.firstOrNull { it.tariffCode == agreement.tariffCode }
                        if (tariff != null) {
                            TariffLayout(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = dimension.grid_2,
                                        end = dimension.grid_2,
                                        bottom = dimension.grid_1,
                                    )
                                    .wrapContentHeight(),
                                tariff = tariff,
                                agreement = agreement,
                                showDivider = index < meterPoint.agreements.lastIndex,
                            )
                        }
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
                meterPoint.meterSerialNumbers.forEach { meterSerialNumber ->
                    MeterSerialNumberEntry(
                        selectedMpan = selectedMpan,
                        selectedMeterSerialNumber = selectedMeterSerialNumber,
                        mpan = meterPoint.mpan,
                        meterSerialNumber = meterSerialNumber,
                        meterSerialNumberTextStyle = meterSerialNumberTextStyle,
                        onMeterSerialNumberSelected = { onMeterSerialNumberSelected(meterPoint.mpan, meterSerialNumber) },
                    )
                }
            }
        }
    }
}
