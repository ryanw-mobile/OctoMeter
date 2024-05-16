/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

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
import com.rwmobi.kunigami.domain.model.Agreement
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
internal fun TariffLayoutCompact(
    modifier: Modifier = Modifier,
    currentAgreement: Agreement,
    tariff: Tariff,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier,
    ) {
        Text(
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            text = tariff.displayName,
        )
        Text(
            style = MaterialTheme.typography.titleSmall,
            text = tariff.fullName,
        )

        Text(
            style = MaterialTheme.typography.bodySmall,
            text = tariff.code,
        )

        Spacer(modifier = Modifier.height(height = dimension.grid_2))

        Text(
            text = "From ${currentAgreement.validFrom.toLocalDateTime(TimeZone.currentSystemDefault()).date} to ${currentAgreement.validTo?.toLocalDateTime(TimeZone.currentSystemDefault())?.date}",
        )

        Spacer(modifier = Modifier.height(height = dimension.grid_3))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            Column(
                modifier = Modifier.weight(1f)
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
                    text = "Unit Rate\n(p/kWh)",
                )
            }

            Column(
                modifier = Modifier.weight(1f)
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
                    text = "Standing Charge\n(p/day)",
                )
            }
        }
    }
}
