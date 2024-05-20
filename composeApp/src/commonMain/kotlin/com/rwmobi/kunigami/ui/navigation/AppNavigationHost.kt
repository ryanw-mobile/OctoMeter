/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rwmobi.kunigami.ui.destinations.account.AccountScreen
import com.rwmobi.kunigami.ui.destinations.account.AccountUIEvent
import com.rwmobi.kunigami.ui.destinations.agile.AgileScreen
import com.rwmobi.kunigami.ui.destinations.agile.AgileUIEvent
import com.rwmobi.kunigami.ui.destinations.tariffs.TariffsScreen
import com.rwmobi.kunigami.ui.destinations.tariffs.TariffsUIEvent
import com.rwmobi.kunigami.ui.destinations.usage.UsageScreen
import com.rwmobi.kunigami.ui.destinations.usage.UsageUIEvent
import com.rwmobi.kunigami.ui.utils.collectAsStateMultiplatform
import com.rwmobi.kunigami.ui.utils.getScreenSizeInfo
import com.rwmobi.kunigami.ui.viewmodels.AccountViewModel
import com.rwmobi.kunigami.ui.viewmodels.AgileViewModel
import com.rwmobi.kunigami.ui.viewmodels.TariffsViewModel
import com.rwmobi.kunigami.ui.viewmodels.UsageViewModel
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun AppNavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    lastDoubleTappedNavItem: AppNavigationItem?,
    onShowSnackbar: suspend (String) -> Unit,
    onScrolledToTop: (AppNavigationItem) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppNavigationItem.USAGE.name,
    ) {
        composable(route = AppNavigationItem.USAGE.name) {
            val viewModel: UsageViewModel = viewModel { getKoin().get() }
            val uiState by viewModel.uiState.collectAsStateMultiplatform()

            // workaround: Issue with iOS we have to do it here
            val screenSizeInfo = getScreenSizeInfo()
            viewModel.notifyScreenSizeChanged(screenSizeInfo = screenSizeInfo)

            UsageScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                uiEvent = UsageUIEvent(
                    onRefresh = viewModel::refresh,
                    onErrorShown = viewModel::errorShown,
                    onShowSnackbar = onShowSnackbar,
                ),
            )
        }

        composable(route = AppNavigationItem.AGILE.name) {
            val viewModel: AgileViewModel = viewModel { getKoin().get() }
            val uiState by viewModel.uiState.collectAsStateMultiplatform()

            // workaround: Issue with iOS we have to do it here
            val screenSizeInfo = getScreenSizeInfo()
            viewModel.notifyScreenSizeChanged(screenSizeInfo = screenSizeInfo)

            AgileScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                uiEvent = AgileUIEvent(
                    onRefresh = viewModel::refresh,
                    onErrorShown = viewModel::errorShown,
                    onShowSnackbar = onShowSnackbar,
                ),
            )
        }

        composable(route = AppNavigationItem.TARIFFS.name) {
            val viewModel: TariffsViewModel = viewModel { getKoin().get() }
            val uiState by viewModel.uiState.collectAsStateMultiplatform()

            TariffsScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                uiEvent = TariffsUIEvent(
                    onRefresh = viewModel::refresh,
                    onProductItemClick = {}, // TODO: Not sure where to go
                    onErrorShown = viewModel::errorShown,
                    onShowSnackbar = onShowSnackbar,
                ),
            )
        }

        composable(route = AppNavigationItem.ACCOUNT.name) {
            val viewModel: AccountViewModel = viewModel { getKoin().get() }
            val uiState by viewModel.uiState.collectAsStateMultiplatform()
            viewModel.notifyWindowSizeClassChanged(windowSizeClass = windowSizeClass)

            AccountScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                uiEvent = AccountUIEvent(
                    onRefresh = viewModel::refresh,
                    onUpdateApiKeyClicked = {}, // TODO: Dialog
                    onClearCredentialButtonClicked = viewModel::clearCredentials,
                    onSubmitCredentials = viewModel::submitCredentials,
                    onMeterSerialNumberSelected = viewModel::updateMeterSerialNumber,
                    onErrorShown = viewModel::errorShown,
                    onShowSnackbar = onShowSnackbar,
                ),
            )
        }
    }
}
