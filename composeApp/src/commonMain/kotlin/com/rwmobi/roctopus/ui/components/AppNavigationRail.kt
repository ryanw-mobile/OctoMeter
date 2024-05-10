/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.roctopus.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rwmobi.roctopus.ui.navigation.AppNavigationItem
import com.rwmobi.roctopus.ui.theme.getDimension
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roctopus.composeapp.generated.resources.Res
import roctopus.composeapp.generated.resources.content_description_navigation_rail

@Composable
fun AppNavigationRail(
    modifier: Modifier = Modifier,
    navController: NavController,
    onCurrentRouteSecondTapped: (item: AppNavigationItem) -> Unit,
) {
    val navigationRailContentDescription = stringResource(Res.string.content_description_navigation_rail)
    NavigationRail(
        modifier = modifier.semantics {
            contentDescription = navigationRailContentDescription
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val dimension = LocalDensity.current.getDimension()

        Spacer(Modifier.weight(1f))

        for (item in AppNavigationItem.getNavBarItems()) {
            val selected = currentRoute == item.name

            val itemContentDescription = stringResource(item.titleResId)
            NavigationRailItem(
                modifier = Modifier
                    .padding(vertical = dimension.defaultFullPadding)
                    .semantics { contentDescription = itemContentDescription },
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

        Spacer(Modifier.weight(1f))
    }
}
//
// @PreviewLightDark
// @Composable
// private fun NavigationRailPreview() {
//    DAZNCodeChallengeTheme {
//        Surface {
//            AppNavigationRail(
//                modifier = Modifier
//                    .wrapContentHeight()
//                    .padding(0.dp),
//                navController = rememberNavController(),
//                onCurrentRouteSecondTapped = {},
//            )
//        }
//    }
// }
