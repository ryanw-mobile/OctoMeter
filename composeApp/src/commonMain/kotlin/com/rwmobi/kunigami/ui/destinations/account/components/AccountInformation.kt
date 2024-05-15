/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.model.Account
import com.rwmobi.kunigami.domain.model.Agreement
import com.rwmobi.kunigami.domain.model.ElectricityMeterPoint
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

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
            Spacer(modifier = Modifier.size(size = dimension.grid_2))

            Text("$fullAddress")

            Spacer(modifier = Modifier.size(size = dimension.grid_2))

            movedInAt?.let {
                Text("Moved in at: ${it.toLocalDateTime(TimeZone.currentSystemDefault()).date}")
            }
            movedOutAt?.let {
                Text("Moved out at: ${it.toLocalDateTime(TimeZone.currentSystemDefault()).date}")
            }

            Spacer(modifier = Modifier.size(size = dimension.grid_3))

            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.size(size = dimension.grid_3))

            Text(
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                text = "Account $accountNumber with ${electricityMeterPoints.size} electricity meter point(s):",
            )

            electricityMeterPoints.forEach { meterPoint ->

                Spacer(modifier = Modifier.size(size = dimension.grid_3))

                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = "MPAN ${meterPoint.mpan} with ${meterPoint.meterSerialNumbers.size} meter(s)",
                )

                Spacer(modifier = Modifier.size(size = dimension.grid_1))

                meterPoint.meterSerialNumbers.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = MaterialTheme.shapes.small)
                            .background(color = MaterialTheme.colorScheme.surfaceContainer)
                            .requiredHeight(height = dimension.minTouchTarget),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = dimension.grid_2),
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            text = it,
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = dimension.grid_2)
                                .clip(shape = MaterialTheme.shapes.large)
                                .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                                .padding(
                                    horizontal = dimension.grid_2,
                                    vertical = dimension.grid_1,
                                ),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            text = "SELECTED",
                        )
                    }

                    Spacer(modifier = Modifier.size(size = dimension.grid_1))
                }

                Spacer(modifier = Modifier.size(size = dimension.grid_1))

                Text(
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    text = "Your tariff:",
                )
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = tariff?.displayName ?: "",
                )
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = tariff?.fullName ?: "",
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "(${tariff?.code})",
                )

                Spacer(modifier = Modifier.size(size = dimension.grid_2))

                Text(
                    text = "From ${meterPoint.currentAgreement.validFrom.toLocalDateTime(TimeZone.currentSystemDefault()).date} to ${meterPoint.currentAgreement.validTo?.toLocalDateTime(TimeZone.currentSystemDefault())?.date}",
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

@Preview
@Composable
private fun AccountInformationPreview() {
    AppTheme {
        Surface {
            AccountInformation(
                modifier = Modifier.padding(all = 32.dp),
                account = Account(
                    id = 8638,
                    accountNumber = "A-1234A1B1",
                    fullAddress = "Address line 1\nAddress line 2\nAddress line 3\nAddress line 4",
                    movedInAt = Clock.System.now(),
                    movedOutAt = null,
                    electricityMeterPoints = listOf(
                        ElectricityMeterPoint(
                            mpan = "1200000345678",
                            meterSerialNumbers = listOf("11A1234567"),
                            currentAgreement = Agreement(
                                tariffCode = "E-1R-AGILE-FLEX-22-11-25-A",
                                validFrom = Clock.System.now(),
                                validTo = Clock.System.now().plus(Duration.parse("365d")),
                            ),
                        ),
                    ),
                ),
                tariff = Tariff(
                    code = "E-1R-AGILE-FLEX-22-11-25-A",
                    fullName = "Octopus 12M Fixed April 2024 v1",
                    displayName = "Octopus 12M Fixed",
                    vatInclusiveUnitRate = 99.257,
                    vatInclusiveStandingCharge = 94.682,
                ),
            )
        }
    }
}
