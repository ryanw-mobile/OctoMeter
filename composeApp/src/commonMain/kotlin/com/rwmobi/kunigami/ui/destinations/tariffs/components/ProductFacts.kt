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
import com.rwmobi.kunigami.domain.extensions.getLocalDateString
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.ui.components.TagWithIcon
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.datetime.Instant
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.tariffs_available_from
import kunigami.composeapp.generated.resources.tariffs_available_to
import kunigami.composeapp.generated.resources.tariffs_fixed_term_months
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ProductFacts(
    modifier: Modifier = Modifier,
    productDetails: ProductDetails,
) {
    val dimension = getScreenSizeInfo().getDimension()

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

        val availableFromDate = productDetails.availability.start.getLocalDateString()
        val availableTo = if (productDetails.availability.endInclusive != Instant.DISTANT_FUTURE) {
            stringResource(resource = Res.string.tariffs_available_to, productDetails.availability.endInclusive.getLocalDateString())
        } else {
            ""
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(
                resource = Res.string.tariffs_available_from,
                availableFromDate,
                availableTo,
            ),
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
