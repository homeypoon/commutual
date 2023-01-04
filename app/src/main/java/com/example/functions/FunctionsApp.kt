package com.example.functions

import android.content.res.Resources
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.functions.common.composable.BottomNavigationComposable
import com.example.functions.common.snackbar.SnackbarManager
import com.example.functions.screens.edit_post.EditPostScreen
import com.example.functions.screens.home.HomeScreen
import com.example.functions.screens.profile.ProfilePostScreen
import com.example.functions.screens.profile.ProfileScreen
import com.example.functions.screens.splash.SplashScreen
import com.example.functions.ui.theme.FunctionsTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun FunctionsApp() {
    Surface(color = MaterialTheme.colors.background) {
        val appState = rememberAppState()
        val bottomNavState = rememberSaveable { (mutableStateOf(false)) }

        val navBackStackEntry by appState.navController.currentBackStackEntryAsState()

        // Control bottomNav visibility
        when (navBackStackEntry?.destination?.route) {
            EDIT_POST_SCREEN -> {
                bottomNavState.value = true
            }
            HOME_SCREEN -> {
                bottomNavState.value = true
            }
            PROFILE_SCREEN -> {
                bottomNavState.value = true
            }
            SPLASH_SCREEN -> {
                bottomNavState.value = false
            }
            PROFILE_POST_SCREEN -> {
                bottomNavState.value = false
            }
            POSTS_SCREEN -> {
                bottomNavState.value = false
            }
        }

        Scaffold(
            bottomBar = {
                BottomNavigationComposable(
                    navController = appState.navController,
                    bottomNavState = bottomNavState
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = it,
                    modifier = Modifier.padding(8.dp),
                    snackbar = { snackbarData ->
                        Snackbar(snackbarData, contentColor = MaterialTheme.colors.onPrimary)
                    }
                )
            },
            scaffoldState = appState.scaffoldState
        ) { innerPaddingModifier ->
            NavHost(
                navController = appState.navController,
                startDestination = SPLASH_SCREEN,
                modifier = Modifier.padding(innerPaddingModifier)
            ) {
                functionsGraph(appState)
            }
        }

    }
}

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, snackbarManager, resources, coroutineScope) {
        FunctionsAppState(scaffoldState, navController, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.functionsGraph(appState: FunctionsAppState) {
    composable(SPLASH_SCREEN) {
        SplashScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }

    composable(HOME_SCREEN) {
        HomeScreen(
            openScreen = { route -> appState.navigate(route) }
        )
    }
    composable(
        route = "$EDIT_POST_SCREEN$POST_ID_ARG",
        arguments = listOf(navArgument(POST_ID) { defaultValue = POST_DEFAULT_ID })
    ) {
        EditPostScreen(
            openScreen = { route -> appState.clearAndNavigate(route) },
//            popUpScreen = { appState.popUp()},
            postId = it.arguments?.getString(POST_ID) ?: POST_DEFAULT_ID
        )
    }



    composable(PROFILE_SCREEN) {
        ProfileScreen(
            openScreen = { route -> appState.navigate(route) })
    }

    composable(PROFILE_POST_SCREEN) {
        ProfilePostScreen(
            openScreen = { route -> appState.navigate(route) })
    }

}