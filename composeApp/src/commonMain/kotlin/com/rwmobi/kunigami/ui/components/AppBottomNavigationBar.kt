/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.kunigami.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rwmobi.kunigami.ui.navigation.AppDestination
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.content_description_navigation_bar
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppBottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    onCurrentRouteSecondTapped: (item: AppDestination) -> Unit,
) {
    val dimension = LocalDensity.current.getDimension()

    val navigationBarContentDescription = stringResource(Res.string.content_description_navigation_bar)
    NavigationBar(
        modifier = modifier.semantics {
            contentDescription = navigationBarContentDescription
        },
        tonalElevation = 0.dp,
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        for (item in AppDestination.getNavBarDestinations()) {
            val selected = currentRoute == item.name

            val itemContentDescription = stringResource(item.titleResId)
            NavigationBarItem(
                modifier = Modifier.semantics { contentDescription = itemContentDescription },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.name) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) {
                                    inclusive = true
                                }
                            }
                            launchSingleTop = true
                        }
                    } else {
                        onCurrentRouteSecondTapped(item)
                    }
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(size = dimension.navigationIconSize),
                        painter = painterResource(resource = item.iconResId),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text = stringResource(resource = item.titleResId).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            AppBottomNavigationBar(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(0.dp),
                navController = rememberNavController(),
                onCurrentRouteSecondTapped = {},
            )
        }
    }
}
