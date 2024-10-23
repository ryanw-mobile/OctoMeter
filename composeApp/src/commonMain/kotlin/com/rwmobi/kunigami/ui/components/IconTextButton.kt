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

package com.rwmobi.kunigami.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.coin
import org.jetbrains.compose.resources.painterResource

@Composable
fun IconTextButton(
    modifier: Modifier = Modifier,
    icon: Painter,
    text: String,
    colors: ButtonColors = ButtonDefaults.buttonColors().copy(
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ),
    shape: Shape = ButtonDefaults.shape,
    onClick: () -> Unit,
) {
    val currentDensity = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(currentDensity.density, fontScale = 1f),
    ) {
        val dimension = getScreenSizeInfo().getDimension()
        Button(
            modifier = modifier,
            shape = shape,
            colors = colors,
            onClick = onClick,
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(intrinsicSize = IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(ratio = 1f),
                    painter = icon,
                    tint = colors.contentColor,
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.width(width = dimension.grid_1))

                Text(
                    modifier = Modifier.wrapContentHeight(),
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.contentColor,
                    text = text,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        IconTextButton(
            icon = painterResource(resource = Res.drawable.coin),
            text = "Money Generator",
            colors = ButtonDefaults.buttonColors().copy(
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            onClick = {},
        )

        IconTextButton(
            icon = painterResource(resource = Res.drawable.coin),
            text = "Money Generator",
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
            onClick = {},
        )
    }
}
