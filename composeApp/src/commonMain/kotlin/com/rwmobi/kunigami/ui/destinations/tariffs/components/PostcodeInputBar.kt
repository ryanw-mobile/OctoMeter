/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.tariffs.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.content_description_edit_postcode
import kunigami.composeapp.generated.resources.settings_24_regular
import kunigami.composeapp.generated.resources.tariffs_postcode
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PostcodeInputBar(
    modifier: Modifier = Modifier,
    postcode: String,
    onUpdatePostcode: (String) -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()
    var showEditPostcodeDialog by remember { mutableStateOf(false) }

    if (showEditPostcodeDialog) {
        EditPostcodeDialog(
            postcode = postcode,
            onDismiss = { showEditPostcodeDialog = false },
            onUpdatePostcode = { newPostcode ->
                showEditPostcodeDialog = false
                onUpdatePostcode(newPostcode)
            },
        )
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
                .fillMaxWidth()
                .height(height = dimension.minListItemHeight)
                .padding(start = dimension.grid_2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(resource = Res.string.tariffs_postcode, postcode),
            )

            IconButton(
                modifier = Modifier.size(size = dimension.imageButtonSize),
                onClick = { showEditPostcodeDialog = true },
            ) {
                Icon(
                    modifier = Modifier.padding(
                        horizontal = dimension.grid_1,
                        vertical = dimension.grid_0_5,
                    ),
                    painter = painterResource(resource = Res.drawable.settings_24_regular),
                    contentDescription = stringResource(resource = Res.string.content_description_edit_postcode),
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        PostcodeInputBar(
            modifier = Modifier.fillMaxWidth(),
            postcode = "WC1X 0ND",
            onUpdatePostcode = { _ -> },
        )
    }
}
