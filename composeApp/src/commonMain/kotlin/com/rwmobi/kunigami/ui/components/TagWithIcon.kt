/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.model.product.ProductFeature
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TagWithIcon(
    modifier: Modifier = Modifier,
    icon: Painter,
    text: String,
) {
    val dimension = LocalDensity.current.getDimension()

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium,
            )
            .padding(
                horizontal = dimension.grid_1,
                vertical = dimension.grid_0_5,
            ),
    ) {
        Icon(
            modifier = Modifier.size(size = 16.dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            painter = icon,
        )
        Text(
            modifier = Modifier.padding(start = dimension.grid_1),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.labelMedium,
            text = text.uppercase(),
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
private fun TagWithIconPreview() {
    AppTheme {
        TagWithIcon(
            icon = painterResource(resource = ProductFeature.GREEN.iconResource),
            text = stringResource(resource = ProductFeature.GREEN.stringResource),
        )
    }
}
