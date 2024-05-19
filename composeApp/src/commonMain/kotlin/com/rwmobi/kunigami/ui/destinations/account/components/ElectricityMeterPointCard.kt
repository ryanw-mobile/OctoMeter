/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.domain.model.ElectricityMeterPoint
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.ui.components.IconTextButton
import com.rwmobi.kunigami.ui.destinations.account.AccountScreenLayout
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
    tariff: Tariff?,
    requestedLayout: AccountScreenLayout,
    onReloadTariff: () -> Unit,
    onMeterSerialNumberSelected: (mpan: String, meterSerialNumber: String) -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimension.grid_2),
            verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
        ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = stringResource(resource = Res.string.account_mpan, meterPoint.mpan),
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimension.grid_1),
                color = MaterialTheme.colorScheme.inverseSurface,
            )

            if (tariff == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimension.grid_2),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
                ) {
                    Text(text = stringResource(resource = Res.string.account_error_null_tariff, meterPoint.currentAgreement.tariffCode))

                    IconTextButton(
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        icon = painterResource(resource = Res.drawable.reload),
                        text = stringResource(resource = Res.string.retry),
                        onClick = onReloadTariff,
                    )
                }
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
                            agreement = meterPoint.currentAgreement,
                        )
                    } else {
                        TariffLayoutWide(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            tariff = tariff,
                            agreement = meterPoint.currentAgreement,
                        )
                    }
                }
            }

            val meterSerialNumberTextStyle = if (requestedLayout is AccountScreenLayout.Compact) {
                MaterialTheme.typography.labelMedium
            } else {
                MaterialTheme.typography.titleMedium
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
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
