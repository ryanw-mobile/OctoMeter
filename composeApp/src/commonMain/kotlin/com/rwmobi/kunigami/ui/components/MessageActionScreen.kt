/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.ui.theme.getDimension
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
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .padding(all = dimension.grid_4)
                .size(size = dimension.minTouchTarget),
            painter = icon,
            tint = MaterialTheme.colorScheme.error,
            contentDescription = null,
        )

        Text(
            modifier = Modifier.padding(horizontal = dimension.grid_4),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            text = text,
        )

        Spacer(modifier = Modifier.height(height = dimension.grid_1))

        description?.let {
            Text(
                modifier = Modifier.padding(horizontal = dimension.grid_4),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                text = it,
            )
        }

        if (primaryButtonLabel != null && onPrimaryButtonClicked != null) {
            Spacer(modifier = Modifier.height(height = dimension.grid_4))

            Button(
                onClick = onPrimaryButtonClicked,
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = dimension.grid_2),
                    style = MaterialTheme.typography.labelMedium,
                    text = primaryButtonLabel.uppercase(),
                )
            }
        }

        if (secondaryButtonLabel != null && onSecondaryButtonClicked != null) {
            Spacer(modifier = Modifier.height(height = dimension.grid_2))

            TextButton(
                onClick = onSecondaryButtonClicked,
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = dimension.grid_2),
                    style = MaterialTheme.typography.labelMedium,
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
