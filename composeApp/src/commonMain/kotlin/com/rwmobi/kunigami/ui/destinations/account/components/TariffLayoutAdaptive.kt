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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import com.rwmobi.kunigami.domain.extensions.getLocalDateString
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.ui.components.TagWithIcon
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Instant
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_tariff_end_date
import kunigami.composeapp.generated.resources.account_tariff_standing_charge
import kunigami.composeapp.generated.resources.account_tariff_start_date
import kunigami.composeapp.generated.resources.account_tariff_unit_rate
import kunigami.composeapp.generated.resources.agile_product_code_retail_region
import kunigami.composeapp.generated.resources.unknown
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TariffLayoutAdaptive(
    modifier: Modifier = Modifier,
    agreement: Agreement,
    tariff: Tariff,
    useWideLayout: Boolean = false,
) {
    if (useWideLayout) {
        TariffLayoutWide(
            modifier = modifier,
            agreement = agreement,
            tariff = tariff,
        )
    } else {
        TariffLayoutCompact(
            modifier = modifier,
            agreement = agreement,
            tariff = tariff,
        )
    }
}

@Composable
private fun TariffLayoutCompact(
    modifier: Modifier = Modifier,
    agreement: Agreement,
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
            style = MaterialTheme.typography.bodySmall,
            text = tariff.fullName,
        )

        val regionCodeStringResource = tariff.getRetailRegion()?.let {
            stringResource(it.stringResource)
        } ?: stringResource(resource = Res.string.unknown)

        Text(
            style = MaterialTheme.typography.bodySmall,
            text = stringResource(resource = Res.string.agile_product_code_retail_region, tariff.productCode, regionCodeStringResource),
        )

        Spacer(modifier = Modifier.height(height = dimension.grid_2))

        val tariffPeriod = if (agreement.period.endInclusive != Instant.DISTANT_FUTURE) {
            stringResource(
                resource = Res.string.account_tariff_end_date,
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

        if (tariff.isVariable) {
            Spacer(modifier = Modifier.height(height = dimension.grid_2))

            val currentDensity = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(currentDensity.density, fontScale = 1f),
            ) {
                TagWithIcon(
                    icon = painterResource(resource = ProductFeature.VARIABLE.iconResource),
                    text = stringResource(resource = ProductFeature.VARIABLE.stringResource),
                )
            }
        }

        Spacer(modifier = Modifier.height(height = dimension.grid_3))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            if (!tariff.isVariable) {
                tariff.resolveUnitRate().let { unitRate ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(intrinsicSize = IntrinsicSize.Max),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            style = MaterialTheme.typography.displaySmall,
                            text = unitRate.toString(),
                        )

                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            text = stringResource(resource = Res.string.account_tariff_unit_rate),
                        )
                    }
                }
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
}

@Composable
private fun TariffLayoutWide(
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

            val regionCode = tariff.getRetailRegion() ?: stringResource(resource = Res.string.unknown)
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = stringResource(resource = Res.string.agile_product_code_retail_region, tariff.productCode, regionCode),
            )

            Spacer(modifier = Modifier.height(height = dimension.grid_2))

            val tariffPeriod = if (agreement.period.endInclusive != Instant.DISTANT_FUTURE) {
                stringResource(
                    resource = Res.string.account_tariff_end_date,
                    agreement.period.endInclusive.getLocalDateString(),
                )
            } else {
                stringResource(
                    resource = Res.string.account_tariff_start_date,
                    agreement.period.start.getLocalDateString(),
                )
            }

            Text(
                style = MaterialTheme.typography.bodySmall,
                text = tariffPeriod,
            )

            if (tariff.isVariable) {
                Spacer(modifier = Modifier.height(height = dimension.grid_2))

                val currentDensity = LocalDensity.current
                CompositionLocalProvider(
                    LocalDensity provides Density(currentDensity.density, fontScale = 1f),
                ) {
                    TagWithIcon(
                        icon = painterResource(resource = ProductFeature.VARIABLE.iconResource),
                        text = stringResource(resource = ProductFeature.VARIABLE.stringResource),
                    )
                }
            }
        }

        if (!tariff.isVariable) {
            tariff.resolveUnitRate().let { unitRate ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(intrinsicSize = IntrinsicSize.Max),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        style = MaterialTheme.typography.displaySmall,
                        text = unitRate.toString(),
                    )

                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        text = stringResource(resource = Res.string.account_tariff_unit_rate),
                    )
                }
            }
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
