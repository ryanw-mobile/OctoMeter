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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.composehelper.getScreenSizeInfo
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.lightning_fill
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
) {
    val dimension = getScreenSizeInfo().getDimension()

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                coroutineScope {
                    launch {
                        // Consume all touch events
                        awaitPointerEventScope {
                            while (true) {
                                awaitPointerEvent()
                            }
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.large)
                .background(
                    color = Color.DarkGray.copy(alpha = 0.48f),
                )
                .wrapContentSize()
                .padding(all = dimension.grid_2),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            VerticalAnimatedTintedPainterResource(
                modifier = Modifier.size(size = 64.dp),
                painter = painterResource(resource = Res.drawable.lightning_fill),
                color = Color.Yellow,
                durationMillis = 500,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        LoadingScreen()
    }
}
