/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.domain.model.product.ElectricityTariffType
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Instant

internal fun LazyListScope.productDetailsLayout(
    modifier: Modifier = Modifier,
    productDetails: ProductDetails,
) {
    item(key = "productFacts") {
        val dimension = LocalDensity.current.getDimension()
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
        ) {
            ProductFacts(
                modifier = Modifier.widthIn(max = dimension.windowWidthCompact),
                productDetails = productDetails,
            )

            if (!productDetails.electricityTariffs.isNullOrEmpty()) {
                var selectedRegion by remember { mutableStateOf("_A") }
                RegionSelectionBar(
                    modifier = Modifier
                        .padding(horizontal = dimension.grid_1)
                        .widthIn(max = dimension.windowWidthCompact)
                        .clip(shape = MaterialTheme.shapes.extraLarge)
                        .background(color = MaterialTheme.colorScheme.surfaceContainerHighest),
//                        .padding(horizontal = dimension.grid_2, vertical = dimension.grid_0_5),
                    electricityTariffs = productDetails.electricityTariffs,
                    selectedRegion = selectedRegion,
                    onRegionSelected = { key -> selectedRegion = key },
                )

                productDetails.electricityTariffs[selectedRegion]?.let { tariffDetails ->
                    RegionTariffDetails(
                        modifier = Modifier
                            .widthIn(max = dimension.windowWidthCompact)
                            .padding(all = dimension.grid_2),
                        tariffDetails = tariffDetails,
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
                electricityTariffType = ElectricityTariffType.UNKNOWN,
                electricityTariffs = mapOf(),
                availableFrom = Instant.parse("2024-03-31T23:00:00Z"),
                availableTo = null,
                brand = "OCTOPUS_ENERGY",
            ),
        )
    }
}
