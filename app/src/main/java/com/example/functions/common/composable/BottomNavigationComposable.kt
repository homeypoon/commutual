package com.example.functions.common.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.functions.HOME_SCREEN
import com.example.functions.PROFILE_SCREEN
import com.example.functions.R

@Composable
fun BottomNavigationComposable(
    navController: NavController,
    bottomNavState: MutableState<Boolean>
) {
    val navItems = listOf(
        Screen.Home,
        Screen.Profile
    )

    AnimatedVisibility(
        visible = bottomNavState.value,
    ) {
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            navItems.forEach { screen ->
                BottomNavigationItem(
                    icon = { Icon(painter = painterResource(screen.iconId), contentDescription = stringResource(screen.resourceId)) },
                    label = { Text(screen.route) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // re-selecting the same item
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int, @DrawableRes val iconId: Int) {
    object Home : Screen(HOME_SCREEN, R.string.home, R.drawable.ic_home)
    object Profile : Screen(PROFILE_SCREEN, R.string.profile, R.drawable.ic_profile)
}