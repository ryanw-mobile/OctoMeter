/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.ui.theme.AppTheme

@Composable
internal fun Onboarding(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            style = MaterialTheme.typography.displayMedium,
            text = "Welcome Aboard!",
        )

        Text(
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            text = "Are you an Octopus Energy customer with a smart meter installed?",
        )

        Text(
            style = MaterialTheme.typography.titleMedium,
            text = "This app is currently running in demo mode, using simulated local data to replicate authenticated access. This means the electricity usage data displayed is not real. Additionally, the app will default to retail region A when retrieving tariffs.",
        )

        Text(
            style = MaterialTheme.typography.titleMedium,
            text = "If you are a current Octopus Energy customer with a smart meter and have access to your web account, you can generate an API key for this app to pull your smart meter data. Visit: https://octopus.energy/dashboard/new/accounts/personal-details/api-access",
        )

        Text(
            style = MaterialTheme.typography.titleMedium,
            text = "Please remember that your API key is confidential, so don't share it with anyone. If you have concerns, regenerating a new key will invalidate all previously issued keys.",
        )

        Text(
            style = MaterialTheme.typography.titleMedium,
            text = "This app can have read-only access to your account data through the public API managed by Octopus Energy.",
        )
    }
}

@Composable
@Preview
private fun OnboardingPreview() {
    AppTheme {
        Onboarding(
            modifier = Modifier.fillMaxSize().padding(all = 24.dp),
        )
    }
}
