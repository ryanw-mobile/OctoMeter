/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.components.IconTextButton
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_clear_credential_button_cta
import kunigami.composeapp.generated.resources.account_clear_credential_description
import kunigami.composeapp.generated.resources.account_clear_credential_title
import kunigami.composeapp.generated.resources.eraser
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ClearCredentialSectionAdaptive(
    modifier: Modifier = Modifier,
    onClearCredentialButtonClicked: () -> Unit,
    useWideLayout: Boolean = false,
) {
    if (useWideLayout) {
        ClearCredentialSectionWide(
            modifier = modifier,
            onClearCredentialButtonClicked = onClearCredentialButtonClicked,
        )
    } else {
        ClearCredentialSectionCompact(
            modifier = modifier,
            onClearCredentialButtonClicked = onClearCredentialButtonClicked,
        )
    }
}

@Composable
private fun ClearCredentialSectionCompact(
    modifier: Modifier = Modifier,
    onClearCredentialButtonClicked: () -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(all = dimension.grid_2),
    ) {
        Text(
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            text = stringResource(resource = Res.string.account_clear_credential_title),
        )

        Spacer(modifier = Modifier.size(size = dimension.grid_2))

        Text(
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(resource = Res.string.account_clear_credential_description),
        )

        Spacer(modifier = Modifier.size(size = dimension.grid_4))

        IconTextButton(
            modifier = Modifier.align(alignment = Alignment.End),
            icon = painterResource(resource = Res.drawable.eraser),
            text = stringResource(resource = Res.string.account_clear_credential_button_cta),
            onClick = onClearCredentialButtonClicked,
        )
    }
}

@Composable
private fun ClearCredentialSectionWide(
    modifier: Modifier = Modifier,
    onClearCredentialButtonClicked: () -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(all = dimension.grid_2),
    ) {
        Text(
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            text = stringResource(resource = Res.string.account_clear_credential_title),
        )

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = dimension.grid_1),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(resource = Res.string.account_clear_credential_description),
            )

            Spacer(modifier = Modifier.size(size = dimension.grid_4))

            IconTextButton(
                icon = painterResource(resource = Res.drawable.eraser),
                text = stringResource(resource = Res.string.account_clear_credential_button_cta),
                onClick = onClearCredentialButtonClicked,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface(modifier = Modifier.padding(all = 16.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            ) {
                ClearCredentialSectionAdaptive(
                    modifier = Modifier.fillMaxWidth(),
                    onClearCredentialButtonClicked = {},
                    useWideLayout = false,
                )

                ClearCredentialSectionAdaptive(
                    modifier = Modifier.fillMaxWidth(),
                    onClearCredentialButtonClicked = {},
                    useWideLayout = true,
                )
            }
        }
    }
}
