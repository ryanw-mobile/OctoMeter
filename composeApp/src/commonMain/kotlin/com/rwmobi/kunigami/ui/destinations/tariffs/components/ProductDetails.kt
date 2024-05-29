/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class, ExperimentalLayoutApi::class)

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import com.rwmobi.kunigami.domain.extensions.toLocalDateString
import com.rwmobi.kunigami.domain.model.product.ElectricityTariffType
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.TagWithIcon
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Instant
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.tariffs_available_from
import kunigami.composeapp.generated.resources.tariffs_available_to
import kunigami.composeapp.generated.resources.tariffs_fixed_term_months
import kunigami.composeapp.generated.resources.tariffs_retail_region
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

internal fun LazyListScope.productDetailsLayout(
    modifier: Modifier = Modifier,
    productDetails: ProductDetails,
) {
    item(key = "productFacts") {
        ProductFacts(
            modifier = modifier,
            productDetails = productDetails,
        )
    }

    if (!productDetails.electricityTariffs.isNullOrEmpty()) {
        item(key = "retailRegions") {
            val dimension = LocalDensity.current.getDimension()
            var selectedRegion by remember { mutableStateOf("_A") }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    .padding(all = dimension.grid_2),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
            ) {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    text = stringResource(resource = Res.string.tariffs_retail_region),
                )
                FlowColumn { }
                FlowRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Center,
                ) {
                    productDetails.electricityTariffs.keys.forEach { key ->
                        if (key != selectedRegion) {
                            TextButton(
                                onClick = { selectedRegion = key },
                            ) {
                                Text(
                                    style = MaterialTheme.typography.labelMedium,
                                    text = key.replace(oldValue = "_", newValue = ""),
                                )
                            }
                        } else {
                            Button(
                                onClick = { },
                            ) {
                                Text(
                                    style = MaterialTheme.typography.labelMedium,
                                    text = key.replace(oldValue = "_", newValue = ""),
                                )
                            }
                        }
                    }
                }
            }

            productDetails.electricityTariffs[selectedRegion]?.let { tariffDetails ->
                Text(text = tariffDetails.toString())
            }
        }
    }
}

@Composable
private fun ProductFacts(
    modifier: Modifier = Modifier,
    productDetails: ProductDetails,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier.padding(
            vertical = dimension.grid_1,
            horizontal = dimension.grid_2,
        ),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            text = productDetails.displayName,
        )

        if (productDetails.fullName != productDetails.displayName) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                style = MaterialTheme.typography.titleMedium,
                text = productDetails.fullName,
            )
        }

        Text(
            modifier = Modifier.wrapContentSize(),
            style = MaterialTheme.typography.labelMedium,
            text = productDetails.code,
        )

        Spacer(modifier = Modifier.size(size = dimension.grid_1))

        val availableFromDate = productDetails.availableFrom.toLocalDateString()
        val availableTo = productDetails.availableTo?.let {
            stringResource(resource = Res.string.tariffs_available_to, it.toLocalDateString())
        } ?: ""

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(resource = Res.string.tariffs_available_from, availableFromDate, availableTo),
        )

        productDetails.term?.let {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(resource = Res.string.tariffs_fixed_term_months, it),
            )
        }

        Spacer(modifier = Modifier.size(size = dimension.grid_1))

        if (productDetails.features.isNotEmpty()) {
            val currentDensity = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(currentDensity.density, fontScale = 1f),
            ) {
                FlowRow(modifier = Modifier.padding(vertical = dimension.grid_1)) {
                    productDetails.features.forEach {
                        TagWithIcon(
                            modifier = Modifier.padding(end = dimension.grid_0_5),
                            icon = painterResource(resource = it.iconResource),
                            text = stringResource(resource = it.stringResource),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(size = dimension.grid_1))

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            text = productDetails.description,
        )
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
