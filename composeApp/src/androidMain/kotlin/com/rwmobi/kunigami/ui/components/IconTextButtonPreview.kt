/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.R
import com.rwmobi.kunigami.ui.theme.AppTheme

/**
 * This is a test case to track Compose Multiplatform resources
 * are not working here.
 * org.jetbrains.compose.resources.MissingResourceException: Missing resource with path: drawable/coin.xml
 */
@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface(modifier = Modifier.padding(all = 8.dp)) {
            IconTextButton(
                modifier = Modifier,
                icon = painterResource(id = R.drawable.ic_android_black_24dp),
                text = "testing",
                onClick = { },
            )
        }
    }
}
