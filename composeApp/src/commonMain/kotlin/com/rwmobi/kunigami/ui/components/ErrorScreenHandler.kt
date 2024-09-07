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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rwmobi.kunigami.ui.model.SpecialErrorScreen
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.access_point_network_off
import kunigami.composeapp.generated.resources.account_clear_credential_title
import kunigami.composeapp.generated.resources.error_badge
import kunigami.composeapp.generated.resources.error_screen_general_error_description
import kunigami.composeapp.generated.resources.error_screen_general_error_description_no_auth
import kunigami.composeapp.generated.resources.error_screen_general_error_title
import kunigami.composeapp.generated.resources.error_screen_general_http_error_description
import kunigami.composeapp.generated.resources.error_screen_general_http_error_description_no_auth
import kunigami.composeapp.generated.resources.error_screen_general_http_error_title
import kunigami.composeapp.generated.resources.error_screen_network_error_description
import kunigami.composeapp.generated.resources.error_screen_network_error_title
import kunigami.composeapp.generated.resources.error_screen_no_data_description
import kunigami.composeapp.generated.resources.error_screen_no_data_description_no_auth
import kunigami.composeapp.generated.resources.error_screen_no_data_title
import kunigami.composeapp.generated.resources.error_screen_unauthorised_description
import kunigami.composeapp.generated.resources.error_screen_unauthorised_title
import kunigami.composeapp.generated.resources.file_dotted
import kunigami.composeapp.generated.resources.folder_lock_outline
import kunigami.composeapp.generated.resources.lan_disconnect
import kunigami.composeapp.generated.resources.retry
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ErrorScreenHandler(
    modifier: Modifier = Modifier,
    specialErrorScreen: SpecialErrorScreen,
    onRefresh: () -> Unit,
    onClearCredential: () -> Unit,
) {
    when (specialErrorScreen) {
        SpecialErrorScreen.NoData -> {
            MessageActionScreen(
                modifier = modifier,
                icon = painterResource(resource = Res.drawable.file_dotted),
                text = stringResource(resource = Res.string.error_screen_no_data_title),
                description = stringResource(resource = Res.string.error_screen_no_data_description),
                primaryButtonLabel = stringResource(resource = Res.string.retry),
                onPrimaryButtonClicked = onRefresh,
                secondaryButtonLabel = stringResource(resource = Res.string.account_clear_credential_title),
                onSecondaryButtonClicked = onClearCredential,
            )
        }

        SpecialErrorScreen.HttpError(statusCode = 401) -> {
            // Unauthorised
            MessageActionScreen(
                modifier = modifier,
                icon = painterResource(resource = Res.drawable.folder_lock_outline),
                text = stringResource(resource = Res.string.error_screen_unauthorised_title),
                description = stringResource(resource = Res.string.error_screen_unauthorised_description),
                primaryButtonLabel = stringResource(resource = Res.string.account_clear_credential_title),
                onPrimaryButtonClicked = onClearCredential,
            )
        }

        is SpecialErrorScreen.HttpError -> {
            // TODO: Refine the errors as we encounter them in the future
            MessageActionScreen(
                modifier = modifier,
                icon = painterResource(resource = Res.drawable.lan_disconnect),
                text = stringResource(resource = Res.string.error_screen_general_http_error_title, specialErrorScreen.statusCode),
                description = stringResource(resource = Res.string.error_screen_general_http_error_description),
                primaryButtonLabel = stringResource(resource = Res.string.retry),
                onPrimaryButtonClicked = onRefresh,
                secondaryButtonLabel = stringResource(resource = Res.string.account_clear_credential_title),
                onSecondaryButtonClicked = onClearCredential,
            )
        }

        SpecialErrorScreen.NetworkError -> {
            MessageActionScreen(
                modifier = modifier,
                icon = painterResource(resource = Res.drawable.access_point_network_off),
                text = stringResource(resource = Res.string.error_screen_network_error_title),
                description = stringResource(resource = Res.string.error_screen_network_error_description),
                primaryButtonLabel = stringResource(resource = Res.string.retry),
                onPrimaryButtonClicked = onRefresh,
            )
        }

        else -> {
            MessageActionScreen(
                modifier = modifier,
                icon = painterResource(resource = Res.drawable.error_badge),
                text = stringResource(resource = Res.string.error_screen_general_error_title),
                description = stringResource(resource = Res.string.error_screen_general_error_description),
                primaryButtonLabel = stringResource(resource = Res.string.retry),
                onPrimaryButtonClicked = onRefresh,
                secondaryButtonLabel = stringResource(resource = Res.string.account_clear_credential_title),
                onSecondaryButtonClicked = onClearCredential,
            )
        }
    }
}

// Variation for sections without authentications. No need to clear credentials
@Composable
internal fun ErrorScreenHandler(
    modifier: Modifier = Modifier,
    specialErrorScreen: SpecialErrorScreen,
    onRefresh: () -> Unit,
) {
    when (specialErrorScreen) {
        SpecialErrorScreen.NoData -> {
            MessageActionScreen(
                modifier = modifier,
                icon = painterResource(resource = Res.drawable.file_dotted),
                text = stringResource(resource = Res.string.error_screen_no_data_title),
                description = stringResource(resource = Res.string.error_screen_no_data_description_no_auth),
                primaryButtonLabel = stringResource(resource = Res.string.retry),
                onPrimaryButtonClicked = onRefresh,
            )
        }

        // Http 401 is considered unusual in this case. This is not right and needs investigation
        is SpecialErrorScreen.HttpError -> {
            // TODO: Refine the errors as we encounter them in the future
            MessageActionScreen(
                modifier = modifier,
                icon = painterResource(resource = Res.drawable.lan_disconnect),
                text = stringResource(resource = Res.string.error_screen_general_http_error_title, specialErrorScreen.statusCode),
                description = stringResource(resource = Res.string.error_screen_general_http_error_description_no_auth),
                primaryButtonLabel = stringResource(resource = Res.string.retry),
                onPrimaryButtonClicked = onRefresh,
            )
        }

        SpecialErrorScreen.NetworkError -> {
            MessageActionScreen(
                modifier = modifier,
                icon = painterResource(resource = Res.drawable.access_point_network_off),
                text = stringResource(resource = Res.string.error_screen_network_error_title),
                description = stringResource(resource = Res.string.error_screen_network_error_description),
                primaryButtonLabel = stringResource(resource = Res.string.retry),
                onPrimaryButtonClicked = onRefresh,
            )
        }

        else -> {
            MessageActionScreen(
                modifier = modifier,
                icon = painterResource(resource = Res.drawable.error_badge),
                text = stringResource(resource = Res.string.error_screen_general_error_title),
                description = stringResource(resource = Res.string.error_screen_general_error_description_no_auth),
                primaryButtonLabel = stringResource(resource = Res.string.retry),
                onPrimaryButtonClicked = onRefresh,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup {
        ErrorScreenHandler(
            specialErrorScreen = SpecialErrorScreen.NoData,
            onRefresh = {},
            onClearCredential = {},
        )

        ErrorScreenHandler(
            specialErrorScreen = SpecialErrorScreen.HttpError(statusCode = 401),
            onRefresh = {},
            onClearCredential = {},
        )

        ErrorScreenHandler(
            specialErrorScreen = SpecialErrorScreen.HttpError(statusCode = 404),
            onRefresh = {},
            onClearCredential = {},
        )

        ErrorScreenHandler(
            specialErrorScreen = SpecialErrorScreen.NetworkError,
            onRefresh = {},
            onClearCredential = {},
        )

        ErrorScreenHandler(
            specialErrorScreen = SpecialErrorScreen.UnknownError,
            onRefresh = {},
            onClearCredential = {},
        )
    }
}
