/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rwmobi.roctopus.ui.destinations.account.AccountScreen
import com.rwmobi.roctopus.ui.destinations.onboarding.OnboardingScreen
import com.rwmobi.roctopus.ui.destinations.tariffs.TariffsScreen
import com.rwmobi.roctopus.ui.destinations.usage.UsageScreen
import com.rwmobi.roctopus.ui.viewmodels.AccountViewModel

@Composable
fun AppNavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    lastDoubleTappedNavItem: AppNavigationItem?,
    onShowSnackbar: suspend (String) -> Unit,
    onScrolledToTop: (AppNavigationItem) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppNavigationItem.Tariffs.name,
    ) {
        composable(route = AppNavigationItem.Onboarding.name) {
            OnboardingScreen(
                modifier = Modifier.fillMaxSize(),
            )
        }

        composable(route = AppNavigationItem.Usage.name) {
            UsageScreen(
                modifier = Modifier.fillMaxSize(),
            )
        }

        composable(route = AppNavigationItem.Tariffs.name) {
            TariffsScreen(
                modifier = Modifier.fillMaxSize(),
            )
        }

        composable(route = AppNavigationItem.Account.name) {
            val accountViewModel = viewModel { AccountViewModel() }
            AccountScreen(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
