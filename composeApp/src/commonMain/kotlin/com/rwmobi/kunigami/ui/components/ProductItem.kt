/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class)

package com.rwmobi.kunigami.ui.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import com.rwmobi.kunigami.domain.model.product.Product
import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier.padding(
            vertical = dimension.grid_1,
            horizontal = dimension.grid_2,
        ),
    ) {
        Text(
            modifier = Modifier.wrapContentSize(),
            style = MaterialTheme.typography.labelMedium,
            text = product.code,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            text = product.displayName,
        )

        if (product.fullName != product.displayName) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                style = MaterialTheme.typography.titleSmall,
                text = product.fullName,
            )
        }

        Spacer(modifier = Modifier.size(size = dimension.grid_1))

        val availableFromDate = product.availableFrom.toLocalDateTime(TimeZone.currentSystemDefault())
        val availableTo = product.availableTo?.let {
            val localDateTime = it.toLocalDateTime(TimeZone.currentSystemDefault())
            "to ${localDateTime.date}"
        } ?: ""

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            text = "Available from ${availableFromDate.date} $availableTo",
        )

        product.term?.let {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                text = "Fixed term $it months",
            )
        }

        Spacer(modifier = Modifier.size(size = dimension.grid_1))

        if (product.features.isNotEmpty()) {
            val currentDensity = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(currentDensity.density, fontScale = 1f),
            ) {
                FlowRow(modifier = Modifier.padding(vertical = dimension.grid_1)) {
                    product.features.forEach {
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
            text = product.description,
        )
    }
}

@Preview
@Composable
private fun ProductItemPreview() {
    CommonPreviewSetup {
        ProductItem(
            modifier = Modifier.fillMaxWidth(),
            product = Product(
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
