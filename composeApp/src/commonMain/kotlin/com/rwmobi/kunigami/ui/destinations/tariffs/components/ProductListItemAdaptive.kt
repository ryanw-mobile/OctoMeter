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

@file:OptIn(ExperimentalLayoutApi::class)

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import com.rwmobi.kunigami.domain.model.product.ProductDirection
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.TagWithIcon
import com.rwmobi.kunigami.ui.theme.AppTheme
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.tariffs_fixed_term_months
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
internal fun ProductListItemAdaptive(
    modifier: Modifier = Modifier,
    productSummary: ProductSummary,
    useWideLayout: Boolean = false,
) {
    if (useWideLayout) {
        ProductListItemWide(
            modifier = modifier,
            productSummary = productSummary,
        )
    } else {
        ProductListItemCompact(
            modifier = modifier,
            productSummary = productSummary,
        )
    }
}

@Composable
private fun ProductListItemCompact(
    modifier: Modifier = Modifier,
    productSummary: ProductSummary,
) {
    Column(
        modifier = modifier.padding(
            vertical = AppTheme.dimens.grid_1,
            horizontal = AppTheme.dimens.grid_2,
        ),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            style = AppTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            text = productSummary.displayName,
        )

        if (productSummary.fullName != productSummary.displayName) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                style = AppTheme.typography.titleSmall,
                text = productSummary.fullName,
            )
        }

        Spacer(modifier = Modifier.size(size = AppTheme.dimens.grid_1))

        productSummary.term?.let {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = AppTheme.typography.bodyMedium,
                text = stringResource(resource = Res.string.tariffs_fixed_term_months, it),
            )

            Spacer(modifier = Modifier.size(size = AppTheme.dimens.grid_1))
        }

        Spacer(modifier = Modifier.size(size = AppTheme.dimens.grid_1))

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = AppTheme.typography.bodyMedium,
            text = productSummary.description,
        )

        Spacer(modifier = Modifier.size(size = AppTheme.dimens.grid_1))

        if (productSummary.features.isNotEmpty()) {
            ProductFeaturesFlowRow(
                productSummary = productSummary,
            )
        }
    }
}

@Composable
internal fun ProductListItemWide(
    modifier: Modifier = Modifier,
    productSummary: ProductSummary,
) {
    Row(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Min)
            .padding(
                vertical = AppTheme.dimens.grid_1,
                horizontal = AppTheme.dimens.grid_2,
            ),
        horizontalArrangement = Arrangement.spacedBy(space = AppTheme.dimens.grid_2),
    ) {
        Column(
            modifier = Modifier
                .weight(weight = 1f)
                .fillMaxHeight(),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = AppTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                text = productSummary.displayName,
            )

            if (productSummary.fullName != productSummary.displayName) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = AppTheme.typography.titleSmall,
                    text = productSummary.fullName,
                )
            }

            Spacer(modifier = Modifier.size(size = AppTheme.dimens.grid_1))

            productSummary.term?.let {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = AppTheme.typography.bodyMedium,
                    text = stringResource(resource = Res.string.tariffs_fixed_term_months, it),
                )

                Spacer(modifier = Modifier.size(size = AppTheme.dimens.grid_1))
            }
        }

        Column(
            modifier = Modifier
                .weight(weight = 2f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.grid_1),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = AppTheme.typography.bodyMedium,
                text = productSummary.description,
            )

            if (productSummary.features.isNotEmpty()) {
                ProductFeaturesFlowRow(
                    productSummary = productSummary,
                )
            }
        }
    }
}

@Composable
private fun ProductFeaturesFlowRow(
    modifier: Modifier = Modifier,
    productSummary: ProductSummary,
) {
    val currentDensity = LocalDensity.current
    CompositionLocalProvider(LocalDensity provides Density(currentDensity.density, fontScale = 1f)) {
        FlowRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.dimens.grid_1),
            verticalArrangement = Arrangement.spacedBy(space = AppTheme.dimens.grid_1),
            horizontalArrangement = Arrangement.End,
        ) {
            productSummary.features.forEach {
                TagWithIcon(
                    modifier = Modifier.padding(end = AppTheme.dimens.grid_0_5),
                    icon = painterResource(resource = it.iconResource),
                    text = stringResource(resource = it.stringResource),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        ProductListItemAdaptive(
            modifier = Modifier.fillMaxWidth(),
            useWideLayout = false,
            productSummary = ProductSummary(
                code = "AGILE-24-04-03",
                direction = ProductDirection.IMPORT,
                fullName = "Agile Octopus April 2024 v1",
                displayName = "Agile Octopus",
                description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
                features = listOf(ProductFeature.VARIABLE, ProductFeature.GREEN),
                term = 12,
                availability = Instant.parse("2024-03-31T23:00:00Z")..Instant.DISTANT_FUTURE,
                brand = "OCTOPUS_ENERGY",
            ),
        )

        ProductListItemAdaptive(
            modifier = Modifier.fillMaxWidth(),
            useWideLayout = true,
            productSummary = ProductSummary(
                code = "AGILE-24-04-03",
                direction = ProductDirection.IMPORT,
                fullName = "Agile Octopus April 2024 v1",
                displayName = "Agile Octopus",
                description = "With Agile Octopus, you get access to half-hourly energy prices, tied to wholesale prices and updated daily.  The unit rate is capped at 100p/kWh (including VAT).",
                features = listOf(ProductFeature.VARIABLE, ProductFeature.GREEN),
                term = 12,
                availability = Instant.parse("2024-03-31T23:00:00Z")..Instant.DISTANT_FUTURE,
                brand = "OCTOPUS_ENERGY",
            ),
        )
    }
}
