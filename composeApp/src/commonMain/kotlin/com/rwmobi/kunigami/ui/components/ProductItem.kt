/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class)

package com.rwmobi.kunigami.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.domain.model.Product
import com.rwmobi.kunigami.domain.model.ProductDirection
import com.rwmobi.kunigami.domain.model.ProductFeature
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import com.rwmobi.kunigami.ui.utils.Preview
import kotlinx.datetime.Instant
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
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            text = product.fullName,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            text = product.description,
        )

        if (product.features.isNotEmpty()) {
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
}

@Preview
@Composable
private fun ProductItemPreview() {
    AppTheme {
        ProductItem(
            modifier = Modifier.fillMaxWidth(),
            product = Product(
                code = "Annie",
                direction = ProductDirection.IMPORT,
                fullName = "Alfredo",
                displayName = "Stephone",
                description = "Tarryn",
                features = listOf(ProductFeature.GREEN),
                term = null,
                availableFrom = Instant.parse("2024-03-31T23:00:00Z"),
                availableTo = null,
                brand = "Phong",
            ),
        )
    }
}
