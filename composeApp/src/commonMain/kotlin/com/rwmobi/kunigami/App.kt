/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rwmobi.kunigami.ui.components.AppBottomNavigationBar
import com.rwmobi.kunigami.ui.components.AppNavigationRail
import com.rwmobi.kunigami.ui.navigation.AppDestination
import com.rwmobi.kunigami.ui.navigation.AppNavigationHost
import com.rwmobi.kunigami.ui.theme.AppTheme
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@Immutable
private enum class NavigationLayoutType {
    BOTTOM_NAVIGATION,
    NAVIGATION_RAIL,
    FULL_SCREEN,
}

private fun WindowSizeClass.calculateNavigationLayout(currentRoute: String?): NavigationLayoutType {
//    if (currentRoute != null && currentRoute == AppNavigationItem.Onboarding.name) {
//        return NavigationLayoutType.FULL_SCREEN
//    }

    return when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            NavigationLayoutType.BOTTOM_NAVIGATION
        }

        else -> {
            // WindowWidthSizeClass.Medium, -- tablet portrait
            // WindowWidthSizeClass.Expanded, -- phone landscape mode
            NavigationLayoutType.NAVIGATION_RAIL
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun App(
    androidStatusBarSideEffect: @Composable ((statusBarColor: Int, isDarkTheme: Boolean) -> Unit)? = null,
) {
    val windowSizeClass = calculateWindowSizeClass()
    val lastDoubleTappedNavItem = remember { mutableStateOf<AppDestination?>(null) }
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navigationLayoutType = windowSizeClass.calculateNavigationLayout(
        currentRoute = currentRoute,
    )

    AppTheme(
        androidStatusBarSideEffect = androidStatusBarSideEffect,
    ) {
        Surface {
            Scaffold(
                modifier = Modifier
                    .safeDrawingPadding()
                    .fillMaxSize(),
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                    )
                },
                bottomBar = {
                    AnimatedVisibility(
                        visible = (navigationLayoutType == NavigationLayoutType.BOTTOM_NAVIGATION),
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = shrinkVertically() + fadeOut(),
                    ) {
                        Column {
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            )

                            AppBottomNavigationBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                navController = navController,
                                onCurrentRouteSecondTapped = { lastDoubleTappedNavItem.value = it },
                            )
                        }
                    }
                },
            ) { paddingValues ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .padding(paddingValues),
                ) {
                    AnimatedVisibility(
                        visible = (navigationLayoutType == NavigationLayoutType.NAVIGATION_RAIL),
                        enter = slideInHorizontally(initialOffsetX = { -it }),
                        exit = shrinkHorizontally() + fadeOut(),
                    ) {
                        Row {
                            AppNavigationRail(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .wrapContentWidth(),
                                navController = navController,
                                onCurrentRouteSecondTapped = { lastDoubleTappedNavItem.value = it },
                            )

                            VerticalDivider(
                                modifier = Modifier.fillMaxHeight()
                                    .wrapContentWidth(),
                                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            )
                        }
                    }

                    val actionLabel = stringResource(Res.string.ok)
                    AppNavigationHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        lastDoubleTappedNavItem = lastDoubleTappedNavItem.value,
                        onShowSnackbar = { errorMessageText ->
                            snackbarHostState.showSnackbar(
                                message = errorMessageText,
                                actionLabel = actionLabel,
                                duration = SnackbarDuration.Long,
                            )
                        },
                        onScrolledToTop = { lastDoubleTappedNavItem.value = null },
                    )
                }
            }
        }
    }
}
