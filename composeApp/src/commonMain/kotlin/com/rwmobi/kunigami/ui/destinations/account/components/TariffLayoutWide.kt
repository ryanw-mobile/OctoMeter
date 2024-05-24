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
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.domain.extensions.toLocalDateString
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_tariff_end_date
import kunigami.composeapp.generated.resources.account_tariff_standing_charge
import kunigami.composeapp.generated.resources.account_tariff_start_date
import kunigami.composeapp.generated.resources.account_tariff_unit_rate
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TariffLayoutWide(
    modifier: Modifier = Modifier,
    tariff: Tariff,
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
                text = tariff.displayName,
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = tariff.fullName,
            )

            Text(
                style = MaterialTheme.typography.bodySmall,
                text = tariff.productCode,
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
                text = tariff.vatInclusiveUnitRate.toString(),
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
                text = tariff.vatInclusiveStandingCharge.toString(),
            )

            Text(
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                text = stringResource(resource = Res.string.account_tariff_standing_charge),
            )
        }
    }
}
