/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.domain.extensions.getLocalDateString
import com.rwmobi.kunigami.domain.extensions.roundToTwoDecimalPlaces
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Instant
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_tariff_date_range
import kunigami.composeapp.generated.resources.account_tariff_day_unit_rate
import kunigami.composeapp.generated.resources.account_tariff_night_unit_rate
import kunigami.composeapp.generated.resources.account_tariff_off_peak_unit_rate
import kunigami.composeapp.generated.resources.account_tariff_standing_charge
import kunigami.composeapp.generated.resources.account_tariff_start_date
import kunigami.composeapp.generated.resources.account_tariff_unit_rate
import kunigami.composeapp.generated.resources.agile_product_code_retail_region
import kunigami.composeapp.generated.resources.tariffs_agile_price_cap
import kunigami.composeapp.generated.resources.tariffs_half_hourly_rate
import kunigami.composeapp.generated.resources.unknown
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TariffLayout(
    modifier: Modifier = Modifier,
    agreement: Agreement,
    showDivider: Boolean = false,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = agreement.displayName,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            text = agreement.fullName,
        )

        val regionCodeStringResource = Tariff.getRetailRegion(tariffCode = agreement.tariffCode)?.let {
            stringResource(it.stringResource)
        } ?: stringResource(resource = Res.string.unknown)

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            text = stringResource(resource = Res.string.agile_product_code_retail_region, agreement.tariffCode, regionCodeStringResource),
        )

        Spacer(modifier = Modifier.height(height = dimension.grid_2))

        val tariffPeriod = if (agreement.period.endInclusive != Instant.DISTANT_FUTURE) {
            stringResource(
                resource = Res.string.account_tariff_date_range,
                agreement.period.start.getLocalDateString(),
                agreement.period.endInclusive.getLocalDateString(),
            )
        } else {
            stringResource(
                resource = Res.string.account_tariff_start_date,
                agreement.period.start.getLocalDateString(),
            )
        }

        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = tariffPeriod,
        )

        Spacer(modifier = Modifier.height(height = dimension.grid_1))

        if (agreement.isHalfHourlyTariff) {
            showHalfHourlyRate()
        }

        RateRow(
            label = stringResource(resource = Res.string.account_tariff_standing_charge),
            rate = agreement.vatInclusiveStandingCharge,
        )

        showRates(agreement = agreement)

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimension.grid_2),
            )
        }
    }
}

@Composable
private fun showRates(
    agreement: Agreement,
) {
    val rateInfoList = listOf(
        Res.string.account_tariff_unit_rate to agreement.vatInclusiveStandardUnitRate,
        Res.string.account_tariff_day_unit_rate to agreement.vatInclusiveDayUnitRate,
        Res.string.account_tariff_night_unit_rate to agreement.vatInclusiveNightUnitRate,
        Res.string.account_tariff_off_peak_unit_rate to agreement.vatInclusiveOffPeakRate,
        Res.string.tariffs_agile_price_cap to agreement.agilePriceCap,
    )

    rateInfoList.forEach { (labelResource, rate) ->
        rate?.let {
            RateRow(
                label = stringResource(resource = labelResource),
                rate = rate,
            )
        }
    }
}

@Composable
private fun RateRow(
    label: String,
    rate: Double,
) {
    val dimension = LocalDensity.current.getDimension()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        Text(
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End,
            text = label,
        )

        Spacer(modifier = Modifier.width(dimension.grid_2))

        Text(
            style = MaterialTheme.typography.titleLarge,
            text = rate.roundToTwoDecimalPlaces().toString(),
        )
    }
}

@Composable
private fun showHalfHourlyRate() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.End,
        text = stringResource(resource = Res.string.tariffs_half_hourly_rate),
    )
}
