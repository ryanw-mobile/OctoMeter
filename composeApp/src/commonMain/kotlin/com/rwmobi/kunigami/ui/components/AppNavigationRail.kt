/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
package com.rwmobi.kunigami.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rwmobi.kunigami.ui.navigation.AppDestination
import com.rwmobi.kunigami.ui.theme.getDimension
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.content_description_navigation_rail
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppNavigationRail(
    modifier: Modifier = Modifier,
    navController: NavController,
    onCurrentRouteSecondTapped: (item: AppDestination) -> Unit,
) {
    val navigationRailContentDescription = stringResource(Res.string.content_description_navigation_rail)
    NavigationRail(
        modifier = modifier.semantics { contentDescription = navigationRailContentDescription },
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val dimension = LocalDensity.current.getDimension()

        Spacer(Modifier.weight(1f))

        for (item in AppDestination.getNavBarDestinations()) {
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
                            popUpTo(route = AppDestination.getStartDestination().name)
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
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
            )
        }

        Spacer(Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun NavigationRailPreview() {
    CommonPreviewSetup {
        AppNavigationRail(
            modifier = Modifier
                .wrapContentHeight()
                .padding(0.dp),
            navController = rememberNavController(),
            onCurrentRouteSecondTapped = {},
        )
    }
}
