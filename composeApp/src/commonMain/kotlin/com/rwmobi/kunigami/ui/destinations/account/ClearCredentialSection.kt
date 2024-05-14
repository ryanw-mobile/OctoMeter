/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.rwmobi.kunigami.ui.components.IconTextButton
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_clear_credential_button_cta
import kunigami.composeapp.generated.resources.account_clear_credential_description
import kunigami.composeapp.generated.resources.account_clear_credential_title
import kunigami.composeapp.generated.resources.eraser
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun ClearCredentialSection(
    modifier: Modifier = Modifier,
    onClearCredentialButtonClicked: () -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    Row(
        modifier = modifier
            .padding(all = dimension.grid_2)
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(all = dimension.grid_3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            Modifier.weight(1f),
        ) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                text = stringResource(resource = Res.string.account_clear_credential_title),
            )

            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(resource = Res.string.account_clear_credential_description),
            )
        }

        Spacer(modifier = Modifier.size(size = dimension.grid_4))

        IconTextButton(
            icon = painterResource(resource = Res.drawable.eraser),
            text = stringResource(resource = Res.string.account_clear_credential_button_cta),
            onClick = onClearCredentialButtonClicked,
        )
    }
}

@Preview
@Composable
private fun ClearCredentialSectionPreview() {
    AppTheme {
        ClearCredentialSection(
            modifier = Modifier.fillMaxWidth(),
            onClearCredentialButtonClicked = {},
        )
    }
}
