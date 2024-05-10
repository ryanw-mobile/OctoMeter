/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.roctopus.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rwmobi.roctopus.ui.navigation.AppNavigationItem
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roctopus.composeapp.generated.resources.Res
import roctopus.composeapp.generated.resources.content_description_navigation_bar

@Composable
fun AppBottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    onCurrentRouteSecondTapped: (item: AppNavigationItem) -> Unit,
) {
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

        for (item in AppNavigationItem.getNavBarItems()) {
            val selected = currentRoute == item.name

            val itemContentDescription = stringResource(item.titleResId)
            NavigationBarItem(
                modifier = Modifier.semantics { contentDescription = itemContentDescription },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.name) {
                            navController.graph.startDestinationRoute?.let { popUpTo(it) }
                            launchSingleTop = true
                        }
                    } else {
                        onCurrentRouteSecondTapped(item)
                    }
                },
                icon = {
//                    Icon(
//                        painter = painterResource(id = item.iconResId),
//                        contentDescription = null,
//                    )
                },
                label = {
                    Text(
                        text = stringResource(resource = item.titleResId).uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
            )
        }
    }
}
//
// @PreviewLightDark
// @Composable
// private fun Preview() {
//    DAZNCodeChallengeTheme {
//        Surface {
//            AppBottomNavigationBar(
//                modifier = Modifier
//                    .wrapContentHeight()
//                    .padding(0.dp),
//                navController = rememberNavController(),
//                onCurrentRouteSecondTapped = {},
//            )
//        }
//    }
// }
