/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.access_point_network_off
import kunigami.composeapp.generated.resources.account_clear_credential_title
import kunigami.composeapp.generated.resources.error_screen_network_error_description
import kunigami.composeapp.generated.resources.error_screen_network_error_title
import kunigami.composeapp.generated.resources.error_screen_unauthorised_description
import kunigami.composeapp.generated.resources.error_screen_unauthorised_title
import kunigami.composeapp.generated.resources.folder_lock_outline
import kunigami.composeapp.generated.resources.retry
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SpecialErrorScreenRouter(
    modifier: Modifier = Modifier,
    specialErrorScreen: SpecialErrorScreen,
    onRefresh: () -> Unit,
    onClearCredential: () -> Unit,
) {
    when (specialErrorScreen) {
        SpecialErrorScreen.HttpError(statusCode = 401) -> {
            // Unauthorised
            DefaultFailureRetryScreen(
                modifier = modifier,
                icon = painterResource(resource = Res.drawable.folder_lock_outline),
                text = stringResource(resource = Res.string.error_screen_unauthorised_title),
                description = stringResource(resource = Res.string.error_screen_unauthorised_description),
                primaryButtonLabel = stringResource(resource = Res.string.account_clear_credential_title),
                onPrimaryButtonClicked = onClearCredential,
            )
        }

        SpecialErrorScreen.NetworkError -> {
            DefaultFailureRetryScreen(
                modifier = modifier,
                icon = painterResource(resource = Res.drawable.access_point_network_off),
                text = stringResource(resource = Res.string.error_screen_network_error_title),
                description = stringResource(resource = Res.string.error_screen_network_error_description),
                primaryButtonLabel = stringResource(resource = Res.string.retry),
                onPrimaryButtonClicked = onRefresh,
            )
        }

        else -> {}
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        SpecialErrorScreenRouter(
            specialErrorScreen = SpecialErrorScreen.HttpError(statusCode = 401),
            onRefresh = {},
            onClearCredential = {},
        )

        SpecialErrorScreenRouter(
            specialErrorScreen = SpecialErrorScreen.NetworkError,
            onRefresh = {},
            onClearCredential = {},
        )
    }
}
