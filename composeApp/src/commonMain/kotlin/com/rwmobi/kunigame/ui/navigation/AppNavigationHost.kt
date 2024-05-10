/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rwmobi.kunigame.ui.destinations.account.AccountScreen
import com.rwmobi.kunigame.ui.destinations.account.AccountUIEvent
import com.rwmobi.kunigame.ui.destinations.onboarding.OnboardingScreen
import com.rwmobi.kunigame.ui.destinations.onboarding.OnboardingUIEvent
import com.rwmobi.kunigame.ui.destinations.tariffs.TariffsScreen
import com.rwmobi.kunigame.ui.destinations.tariffs.TariffsUIEvent
import com.rwmobi.kunigame.ui.destinations.usage.UsageScreen
import com.rwmobi.kunigame.ui.destinations.usage.UsageUIEvent
import com.rwmobi.kunigame.ui.destinations.usage.UsageUIState
import com.rwmobi.kunigame.ui.utils.collectAsStateMultiplatform
import com.rwmobi.kunigame.ui.viewmodels.AccountViewModel
import com.rwmobi.kunigame.ui.viewmodels.OnboardingViewModel
import com.rwmobi.kunigame.ui.viewmodels.TariffsViewModel
import com.rwmobi.kunigame.ui.viewmodels.UsageViewModel
import org.koin.mp.KoinPlatform.getKoin

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
            val viewModel: OnboardingViewModel = viewModel { getKoin().get() }
            val uiState by viewModel.uiState.collectAsStateMultiplatform()

            OnboardingScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                uiEvent = OnboardingUIEvent(
                    onErrorShown = viewModel::errorShown,
                    onShowSnackbar = onShowSnackbar,
                ),
            )
        }

        composable(route = AppNavigationItem.Usage.name) {
            val viewModel: UsageViewModel = viewModel { getKoin().get() }
            val uiState by viewModel.uiState.collectAsStateMultiplatform()

            UsageScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = UsageUIState(isLoading = false, errorMessages = listOf()),
                uiEvent = UsageUIEvent(
                    onErrorShown = viewModel::errorShown,
                    onShowSnackbar = onShowSnackbar,
                ),
            )
        }

        composable(route = AppNavigationItem.Tariffs.name) {
            val viewModel: TariffsViewModel = viewModel { getKoin().get() }
            val uiState by viewModel.uiState.collectAsStateMultiplatform()

            TariffsScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                uiEvent = TariffsUIEvent(
                    onRefresh = viewModel::refresh,
                    onErrorShown = viewModel::errorShown,
                    onShowSnackbar = onShowSnackbar,
                ),
            )
        }

        composable(route = AppNavigationItem.Account.name) {
            val viewModel: AccountViewModel = viewModel { getKoin().get() }
            val uiState by viewModel.uiState.collectAsStateMultiplatform()

            AccountScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                uiEvent = AccountUIEvent(
                    onErrorShown = viewModel::errorShown,
                    onShowSnackbar = onShowSnackbar,
                ),
            )
        }
    }
}
