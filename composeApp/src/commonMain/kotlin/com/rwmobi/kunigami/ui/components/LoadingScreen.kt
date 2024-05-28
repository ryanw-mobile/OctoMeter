/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.lightning_fill
import kunigami.composeapp.generated.resources.loading
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Preview
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
) {
    val dimension = LocalDensity.current.getDimension()

    Surface(
        modifier = modifier,
        color = Color.DarkGray.copy(
            alpha = 0.8f,
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AnimatedTintedPainterResource(
                    modifier = Modifier.size(size = 80.dp),
                    painter = painterResource(resource = Res.drawable.lightning_fill),
                    color = Color.Yellow,
                    durationMillis = 500,
                )

                Spacer(modifier = Modifier.height(height = dimension.grid_2))

                Text(
                    modifier = Modifier.wrapContentSize(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    text = stringResource(resource = Res.string.loading),
                )
            }
        }
    }
}
