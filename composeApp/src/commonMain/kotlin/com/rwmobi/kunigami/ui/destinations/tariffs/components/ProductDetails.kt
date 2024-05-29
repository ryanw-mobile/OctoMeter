/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class, ExperimentalLayoutApi::class)

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.TagWithIcon
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

internal fun LazyListScope.productDetails(
    modifier: Modifier = Modifier,
    productDetails: ProductDetails,
) {
    item {
        ProductFacts(
            modifier = modifier,
            productDetails = productDetails,
        )
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

        Spacer(modifier = Modifier.size(size = dimension.grid_1))

        Text(
            modifier = Modifier.wrapContentSize(),
            style = MaterialTheme.typography.labelMedium,
            text = productDetails.code,
        )

        Spacer(modifier = Modifier.size(size = dimension.grid_1))

        val availableFromDate = productDetails.availableFrom.toLocalDateTime(TimeZone.currentSystemDefault())
        val availableTo = productDetails.availableTo?.let {
            val localDateTime = it.toLocalDateTime(TimeZone.currentSystemDefault())
            "to ${localDateTime.date}"
        } ?: ""

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            text = "Available from ${availableFromDate.date} $availableTo",
        )

        productDetails.term?.let {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                text = "Fixed term $it months",
            )
        }

        // TODO: Exit fees

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

    HorizontalDivider(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = dimension.grid_2),
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
    )
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
                availableFrom = Instant.parse("2024-03-31T23:00:00Z"),
                availableTo = null,
                brand = "OCTOPUS_ENERGY",
            ),
        )
    }
}
