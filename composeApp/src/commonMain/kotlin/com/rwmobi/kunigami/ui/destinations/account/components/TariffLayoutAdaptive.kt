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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.domain.extensions.toLocalDateString
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.product.TariffSummary
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_tariff_end_date
import kunigami.composeapp.generated.resources.account_tariff_standing_charge
import kunigami.composeapp.generated.resources.account_tariff_start_date
import kunigami.composeapp.generated.resources.account_tariff_unit_rate
import kunigami.composeapp.generated.resources.agile_product_code_retail_region
import kunigami.composeapp.generated.resources.unknown
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TariffLayoutAdaptive(
    modifier: Modifier = Modifier,
    agreement: Agreement,
    tariffSummary: TariffSummary,
    useWideLayout: Boolean = false,
) {
    if (useWideLayout) {
        TariffLayoutWide(
            modifier = modifier,
            agreement = agreement,
            tariffSummary = tariffSummary,
        )
    } else {
        TariffLayoutCompact(
            modifier = modifier,
            agreement = agreement,
            tariffSummary = tariffSummary,
        )
    }
}

@Composable
private fun TariffLayoutCompact(
    modifier: Modifier = Modifier,
    agreement: Agreement,
    tariffSummary: TariffSummary,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier,
    ) {
        Text(
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = tariffSummary.displayName,
        )
        Text(
            style = MaterialTheme.typography.bodySmall,
            text = tariffSummary.fullName,
        )

        val regionCode = tariffSummary.getRetailRegion() ?: stringResource(resource = Res.string.unknown)
        Text(
            style = MaterialTheme.typography.bodySmall,
            text = stringResource(resource = Res.string.agile_product_code_retail_region, tariffSummary.productCode, regionCode),
        )

        Spacer(modifier = Modifier.height(height = dimension.grid_2))

        val tariffPeriod = agreement.validTo?.let {
            stringResource(
                resource = Res.string.account_tariff_end_date,
                it.toLocalDateString(),
            )
        } ?: stringResource(
            resource = Res.string.account_tariff_start_date,
            agreement.validFrom.toLocalDateString(),
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = tariffPeriod,
        )

        Spacer(modifier = Modifier.height(height = dimension.grid_3))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(intrinsicSize = IntrinsicSize.Max),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    style = MaterialTheme.typography.displaySmall,
                    text = tariffSummary.vatInclusiveUnitRate.toString(),
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    text = stringResource(resource = Res.string.account_tariff_unit_rate),
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(intrinsicSize = IntrinsicSize.Max),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    style = MaterialTheme.typography.displaySmall,
                    text = tariffSummary.vatInclusiveStandingCharge.toString(),
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    text = stringResource(resource = Res.string.account_tariff_standing_charge),
                )
            }
        }
    }
}

@Composable
private fun TariffLayoutWide(
    modifier: Modifier = Modifier,
    tariffSummary: TariffSummary,
    agreement: Agreement,
) {
    val dimension = LocalDensity.current.getDimension()

    Row(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .weight(2f)
                .height(intrinsicSize = IntrinsicSize.Max),
        ) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                text = tariffSummary.displayName,
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = tariffSummary.fullName,
            )

            val regionCode = tariffSummary.getRetailRegion() ?: stringResource(resource = Res.string.unknown)
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = stringResource(resource = Res.string.agile_product_code_retail_region, tariffSummary.productCode, regionCode),
            )

            Spacer(modifier = Modifier.height(height = dimension.grid_2))

            val tariffPeriod = agreement.validTo?.let {
                stringResource(
                    resource = Res.string.account_tariff_end_date,
                    it.toLocalDateString(),
                )
            } ?: stringResource(
                resource = Res.string.account_tariff_start_date,
                agreement.validFrom.toLocalDateString(),
            )

            Text(
                style = MaterialTheme.typography.bodySmall,
                text = tariffPeriod,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .height(intrinsicSize = IntrinsicSize.Max),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                style = MaterialTheme.typography.displaySmall,
                text = tariffSummary.vatInclusiveUnitRate.toString(),
            )

            Text(
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                text = stringResource(resource = Res.string.account_tariff_unit_rate),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .height(intrinsicSize = IntrinsicSize.Max),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                style = MaterialTheme.typography.displaySmall,
                text = tariffSummary.vatInclusiveStandingCharge.toString(),
            )

            Text(
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                text = stringResource(resource = Res.string.account_tariff_standing_charge),
            )
        }
    }
}
