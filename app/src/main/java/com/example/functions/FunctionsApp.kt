package com.example.functions

import LoginScreen
import PostDetailsScreen
import android.content.res.Resources
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.functions.common.composable.BottomNavigationComposable
import com.example.functions.common.snackbar.SnackbarManager
import com.example.functions.ui.edit_profile.EditProfileScreen
import com.example.functions.ui.screens.edit_post.EditPostScreen
import com.example.functions.ui.screens.explore.ExploreScreen
import com.example.functions.ui.screens.home.HomeScreen
import com.example.functions.ui.screens.profile.ProfileScreen
import com.example.functions.ui.screens.profile.SettingsScreen
import com.example.functions.ui.screens.profile_post.ProfilePostScreen
import com.example.functions.ui.screens.splash.SplashScreen
import com.example.makeitso.screens.sign_up.SignUpScreen
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
        route = "$PROFILE_POST_SCREEN$POST_ID_ARG",
        arguments = listOf(navArgument(POST_ID) { defaultValue = POST_DEFAULT_ID})
    ) {
        ProfilePostScreen(
            popUpScreen = { appState.popUp()},
            openScreen = { route -> appState.navigate(route) },
            postId = it.arguments?.getString(POST_ID) ?: POST_DEFAULT_ID
        )
    }

    composable(
        route = "$EDIT_POST_SCREEN$POST_ID_ARG",
        arguments = listOf(navArgument(POST_ID) { defaultValue = POST_DEFAULT_ID })
    ) {
        EditPostScreen(
            popUpScreen = { appState.popUp()},
            postId = it.arguments?.getString(POST_ID) ?: POST_DEFAULT_ID
        )
    }

    composable(
        route = "$POST_DETAILS_SCREEN$POST_ID_ARG",
        arguments = listOf(navArgument(POST_ID) { defaultValue = POST_DEFAULT_ID })
    ) {
        PostDetailsScreen(
            openScreen = { route -> appState.navigate(route) },
            postId = it.arguments?.getString(POST_ID) ?: POST_DEFAULT_ID
        )
    }

    composable(EDIT_PROFILE_SCREEN) {
        EditProfileScreen( popUpScreen = { appState.popUp()})
    }

    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(EXPLORE_SCREEN) {
        ExploreScreen(
            openScreen = { route -> appState.navigate(route) })
    }

    composable(PROFILE_SCREEN) {
        ProfileScreen(
            openScreen = { route -> appState.navigate(route) })
    }

    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) }
        )
    }

}