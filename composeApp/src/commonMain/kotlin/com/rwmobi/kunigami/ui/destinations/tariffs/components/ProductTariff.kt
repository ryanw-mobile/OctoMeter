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

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.product.ExitFeesType
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.product.TariffPaymentTerm
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.LabelValueRow
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.day_unit_rate
import kunigami.composeapp.generated.resources.night_unit_rate
import kunigami.composeapp.generated.resources.no
import kunigami.composeapp.generated.resources.off_peak_rate
import kunigami.composeapp.generated.resources.retail_region_unknown
import kunigami.composeapp.generated.resources.standard_unit_rate
import kunigami.composeapp.generated.resources.standing_charge
import kunigami.composeapp.generated.resources.tariffs_direct_debit_monthly
import kunigami.composeapp.generated.resources.tariffs_exit_fees
import kunigami.composeapp.generated.resources.unit_p_day
import kunigami.composeapp.generated.resources.unit_p_kwh
import kunigami.composeapp.generated.resources.unit_pound
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RegionTariff(
    modifier: Modifier,
    tariff: Tariff,
) {
    val dimension = getScreenSizeInfo().getDimension()

    Column(
        modifier = modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
    ) {
        with(tariff) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val region = stringResource(resource = getRetailRegion()?.stringResource ?: Res.string.retail_region_unknown)
                Text(
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    text = region,
                )

                Text(
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    text = tariffCode,
                )
            }

            if (tariffPaymentTerm == TariffPaymentTerm.DIRECT_DEBIT_MONTHLY) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic,
                    text = stringResource(resource = Res.string.tariffs_direct_debit_monthly),
                )
            }

            LabelValueRow(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                label = stringResource(resource = Res.string.tariffs_exit_fees),
                value = if (exitFeesType != ExitFeesType.NONE) {
                    stringResource(
                        resource = Res.string.unit_pound,
                        vatInclusiveExitFees.roundToTwoDecimalPlaces(),
                    )
                } else {
                    stringResource(resource = Res.string.no)
                },
            )

            LabelValueRow(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                label = stringResource(resource = Res.string.standing_charge),
                value = stringResource(
                    resource = Res.string.unit_p_day,
                    vatInclusiveStandingCharge.roundToTwoDecimalPlaces(),
                ),
            )

            vatInclusiveStandardUnitRate?.let { unitRate ->
                LabelValueRow(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    label = stringResource(resource = Res.string.standard_unit_rate),
                    value = stringResource(
                        resource = Res.string.unit_p_kwh,
                        unitRate.roundToTwoDecimalPlaces(),
                    ),
                )
            }

            vatInclusiveDayUnitRate?.let { unitRate ->
                LabelValueRow(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    label = stringResource(resource = Res.string.day_unit_rate),
                    value = stringResource(
                        resource = Res.string.unit_p_kwh,
                        unitRate.roundToTwoDecimalPlaces(),
                    ),
                )
            }

            vatInclusiveNightUnitRate?.let { unitRate ->
                LabelValueRow(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    label = stringResource(resource = Res.string.night_unit_rate),
                    value = stringResource(
                        resource = Res.string.unit_p_kwh,
                        unitRate.roundToTwoDecimalPlaces(),
                    ),
                )
            }

            vatInclusiveOffPeakRate?.let { unitRate ->
                LabelValueRow(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    label = stringResource(resource = Res.string.off_peak_rate),
                    value = stringResource(
                        resource = Res.string.unit_p_kwh,
                        unitRate.roundToTwoDecimalPlaces(),
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        RegionTariff(
            modifier = Modifier.fillMaxSize(),
            tariff = TariffSamples.agileFlex221125,
        )
    }
}
