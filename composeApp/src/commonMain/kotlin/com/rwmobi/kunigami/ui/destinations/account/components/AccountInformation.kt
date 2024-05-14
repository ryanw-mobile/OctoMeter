/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.model.Account
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
internal fun AccountInformation(
    modifier: Modifier = Modifier,
    account: Account,
    tariff: Tariff?,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier,
    ) {
        with(account) {
            Text(text = "Account number: $accountNumber")

            Spacer(modifier = Modifier.size(size = dimension.grid_2))

            Text("$fullAddress")

            Spacer(modifier = Modifier.size(size = dimension.grid_2))

            movedInAt?.let {
                Text("Moved in at: ${it.toLocalDateTime(TimeZone.currentSystemDefault()).date}")
            }
            movedOutAt?.let {
                Text("Moved out at: ${it.toLocalDateTime(TimeZone.currentSystemDefault()).date}")
            }

            Spacer(modifier = Modifier.size(size = dimension.grid_1))

            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            Text(
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                text = "Meter information",
            )

            electricityMeterPoints.forEach { meterPoint ->
                Text("MPAN: ${meterPoint.mpan}")
                Text("Meter serial numbers for this meter point:")
                meterPoint.meterSerialNumbers.forEach {
                    Text(text = "- $it")
                }

                Spacer(modifier = Modifier.size(size = dimension.grid_1))

                Text(
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    text = "Tariff for this Meter point:",
                )
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = tariff?.fullName ?: "",
                )
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = tariff?.displayName ?: "",
                )
                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = meterPoint.currentAgreement.tariffCode,
                )

                Text(
                    text = "From ${meterPoint.currentAgreement.validFrom.toLocalDateTime(TimeZone.currentSystemDefault()).date} to ${meterPoint.currentAgreement.validTo?.toLocalDateTime(TimeZone.currentSystemDefault())?.date}",
                )
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Tariff code: ${tariff?.code}  (All rates inc. VAT)",
                )
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Unit Rate: ${tariff?.vatInclusiveUnitRate ?: ""} p/kWh ",
                )
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Standing charge: £${tariff?.vatInclusiveStandingCharge ?: ""} p/day",
                )
            }
        }

        Spacer(modifier = Modifier.size(size = dimension.grid_3))
    }
}
