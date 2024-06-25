/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.IconTextButton
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_update_api_key
import kunigami.composeapp.generated.resources.key
import kunigami.composeapp.generated.resources.update
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SimpleTitleButtonCard(
    modifier: Modifier = Modifier,
    title: String,
    buttonLabel: String,
    buttonPainter: Painter,
    onButtonClicked: () -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    Row(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(all = dimension.grid_2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            text = title,
        )

        Spacer(modifier = Modifier.size(size = dimension.grid_4))

        IconTextButton(
            icon = buttonPainter,
            text = buttonLabel,
            onClick = onButtonClicked,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        SimpleTitleButtonCard(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(resource = Res.string.account_update_api_key),
            buttonLabel = stringResource(resource = Res.string.update),
            buttonPainter = painterResource(resource = Res.drawable.key),
            onButtonClicked = {},
        )
    }
}
