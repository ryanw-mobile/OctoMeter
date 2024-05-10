/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus

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
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import com.rwmobi.roctopus.ui.components.AppBottomNavigationBar
import com.rwmobi.roctopus.ui.components.AppNavigationRail
import com.rwmobi.roctopus.ui.navigation.AppNavigationHost
import com.rwmobi.roctopus.ui.navigation.AppNavigationItem
import com.rwmobi.roctopus.ui.theme.AppTheme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import roctopus.composeapp.generated.resources.Res
import roctopus.composeapp.generated.resources.ok

private enum class NavigationLayoutType {
    BOTTOM_NAVIGATION,
    NAVIGATION_RAIL,
    FULL_SCREEN,
}

private fun WindowSizeClass.calculateNavigationLayout(currentRoute: String?): NavigationLayoutType {
    if (currentRoute != null && currentRoute == AppNavigationItem.Onboarding.name) {
        return NavigationLayoutType.FULL_SCREEN
    }

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

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
@Preview
fun App(
    strings: List<String> = emptyList(),
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val windowSizeClass = calculateWindowSizeClass()
    val lastDoubleTappedNavItem = remember { mutableStateOf<AppNavigationItem?>(null) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navigationLayoutType = windowSizeClass.calculateNavigationLayout(
        currentRoute = currentRoute,
    )

    Logger.d(tag = "tag", messageString = "This is how we log in this project.")

    AppTheme {
        Surface {
            Row(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(
                    visible = (navigationLayoutType == NavigationLayoutType.NAVIGATION_RAIL),
                    enter = slideInHorizontally(initialOffsetX = { -it }),
                    exit = shrinkHorizontally() + fadeOut(),
                ) {
                    Row {
                        AppNavigationRail(
                            modifier = Modifier.fillMaxHeight(),
                            navController = navController,
                            onCurrentRouteSecondTapped = { lastDoubleTappedNavItem.value = it },
                        )

                        VerticalDivider(
                            modifier = Modifier.fillMaxHeight(),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                        )
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
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
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                )

                                AppBottomNavigationBar(
                                    navController = navController,
                                    onCurrentRouteSecondTapped = { lastDoubleTappedNavItem.value = it },
                                )
                            }
                        }
                    },
                ) { paddingValues ->
                    val actionLabel = stringResource(Res.string.ok)
                    AppNavigationHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
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

//
// //            var showContent by remember { mutableStateOf(false) }
//            Column {
//                Text(text = stringResource(Res.string.app_name))
//                Text(text = "Window Size Class: width = ${windowSizeClass.widthSizeClass}, height = ${windowSizeClass.heightSizeClass}")
// //                Button(onClick = { showContent = !showContent }) {
// //                    Text("Click me!")
// //                }
// //                AnimatedVisibility(showContent) {
// //                    val greeting = remember { Greeting().greet() }
// //                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
// //                        Image(painterResource(Res.drawable.compose_multiplatform), null)
// //                        Text("Compose: $greeting")
// //                    }
// //                }
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .fillMaxHeight(1f),
//                ) {
//                    items(strings) {
//                        Text(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .wrapContentHeight(),
//                            text = it,
//                        )
//                        HorizontalDivider(
//                            modifier = Modifier
//                                .padding(vertical = 8.dp)
//                                .fillMaxWidth(),
//                        )
//                    }
//                }
//            }
        }
    }
}
