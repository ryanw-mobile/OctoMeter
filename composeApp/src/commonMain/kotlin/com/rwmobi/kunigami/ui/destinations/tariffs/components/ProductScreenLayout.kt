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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.rwmobi.kunigami.domain.model.product.ExitFeesType
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.product.TariffPaymentTerm
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.theme.AppTheme
import kotlin.time.Instant

internal fun LazyListScope.productScreenLayout(
    modifier: Modifier = Modifier,
    productDetails: ProductDetails,
) {
    item(key = "productFacts") {
        Column(
            modifier = modifier.padding(horizontal = AppTheme.dimens.grid_2),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = AppTheme.dimens.grid_2),
        ) {
            val blockModifier = Modifier
                .clip(shape = AppTheme.shapes.large)
                .background(AppTheme.colorScheme.surfaceContainer)
                .padding(AppTheme.dimens.grid_1)

            Column(modifier = blockModifier) {
                ProductFacts(
                    modifier = Modifier.widthIn(max = AppTheme.dimens.windowWidthCompact),
                    productDetails = productDetails,
                )
            }

            Column(modifier = blockModifier) {
                productDetails.electricityTariff?.let { tariffDetails ->
                    RegionTariff(
                        modifier = Modifier
                            .widthIn(max = AppTheme.dimens.windowWidthCompact)
                            .padding(all = AppTheme.dimens.grid_2),
                        tariff = tariffDetails,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductItemPreview() {
    CommonPreviewSetup {
        ProductFacts(
            modifier = Modifier.fillMaxWidth(),
            productDetails = ProductDetails(
                code = "AGILE-24-04-03",
                direction = ProductDirection.IMPORT,
                fullName = "Agile Octopus April 2024 v1",
                displayName = "Agile Octopus",
                description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
                features = listOf(ProductFeature.VARIABLE, ProductFeature.GREEN),
                term = 12,
                electricityTariff = Tariff(
                    productCode = "AGILE-24-04-03",
                    fullName = "Agile Octopus November 2022 v1",
                    displayName = "Agile Octopus",
                    description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
                    isVariable = true,
                    availability = Instant.parse("2022-11-25T00:00:00Z")..Instant.parse("2023-12-11T12:00:00Z"),
                    tariffPaymentTerm = TariffPaymentTerm.DIRECT_DEBIT_MONTHLY,
                    tariffCode = "E-1R-AGILE-24-04-03-A",
                    vatInclusiveStandingCharge = 37.296,
                    exitFeesType = ExitFeesType.NONE,
                    vatInclusiveExitFees = 0.0,
                    vatInclusiveStandardUnitRate = 16.5795,
                    vatInclusiveDayUnitRate = null,
                    vatInclusiveNightUnitRate = null,
                    vatInclusiveOffPeakRate = null,
                ),
                availability = Instant.parse("2024-03-31T23:00:00Z")..Instant.DISTANT_FUTURE,
                brand = "OCTOPUS_ENERGY",
            ),
        )
    }
}
