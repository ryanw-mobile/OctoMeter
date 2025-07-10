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

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.kunigami.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.ui.theme.AppTheme
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.wallet
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun MessageActionScreen(
    modifier: Modifier = Modifier,
    icon: Painter,
    text: String,
    description: String? = null,
    primaryButtonLabel: String? = null,
    onPrimaryButtonClicked: (() -> Unit)? = null,
    secondaryButtonLabel: String? = null,
    onSecondaryButtonClicked: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .padding(all = AppTheme.dimens.grid_4)
                .size(size = AppTheme.dimens.minTouchTarget),
            painter = icon,
            tint = AppTheme.colorScheme.error,
            contentDescription = null,
        )

        Text(
            modifier = Modifier.padding(horizontal = AppTheme.dimens.grid_4),
            style = AppTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            text = text,
        )

        Spacer(modifier = Modifier.height(height = AppTheme.dimens.grid_1))

        description?.let {
            Text(
                modifier = Modifier.padding(horizontal = AppTheme.dimens.grid_4),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.titleMedium,
                text = it,
            )
        }

        if (primaryButtonLabel != null && onPrimaryButtonClicked != null) {
            Spacer(modifier = Modifier.height(height = AppTheme.dimens.grid_4))

            Button(
                onClick = onPrimaryButtonClicked,
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = AppTheme.dimens.grid_2),
                    style = AppTheme.typography.labelMedium,
                    text = primaryButtonLabel.uppercase(),
                )
            }
        }

        if (secondaryButtonLabel != null && onSecondaryButtonClicked != null) {
            Spacer(modifier = Modifier.height(height = AppTheme.dimens.grid_2))

            TextButton(
                onClick = onSecondaryButtonClicked,
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = AppTheme.dimens.grid_2),
                    style = AppTheme.typography.labelMedium,
                    text = secondaryButtonLabel,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        MessageActionScreen(
            modifier = Modifier.fillMaxSize(),
            text = "Something's very wrong with my wallet, let's try again?",
            description = "Please check your internet connection?",
            icon = painterResource(Res.drawable.wallet),
            primaryButtonLabel = "Alright, try again",
            onPrimaryButtonClicked = {},
            secondaryButtonLabel = "No, thanks. Goodbye.",
            onSecondaryButtonClicked = {},
        )
    }
}
