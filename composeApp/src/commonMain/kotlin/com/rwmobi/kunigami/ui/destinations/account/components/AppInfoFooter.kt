/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.theme.getDimension
import composeapp.kunigami.BuildConfig
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.account_version_api_disclaimer
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AppInfoFooter(
    modifier: Modifier = Modifier,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            modifier = modifier.padding(
                vertical = dimension.grid_2,
                horizontal = dimension.grid_4,
            ),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            text = stringResource(resource = Res.string.account_version_api_disclaimer, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, BuildConfig.GITHUB_LINK),
        )
    }
}

@Preview
@Composable
private fun AppInfoFooterPreview() {
    CommonPreviewSetup {
        AppInfoFooter(modifier = Modifier.fillMaxWidth())
    }
}
