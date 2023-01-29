package com.example.commutual

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
import com.example.commutual.common.composable.BottomNavigationComposable
import com.example.commutual.common.snackbar.SnackbarManager
import com.example.commutual.ui.screens.edit_post.EditPostScreen
import com.example.commutual.ui.screens.edit_profile.EditProfileScreen
import com.example.commutual.ui.screens.explore.ExploreScreen
import com.example.commutual.ui.screens.home.HomeScreen
import com.example.commutual.ui.screens.login.LoginScreen
import com.example.commutual.ui.screens.post_details.PostDetailsScreen
import com.example.commutual.ui.screens.profile.ProfileScreen
import com.example.commutual.ui.screens.profile.SettingsScreen
import com.example.commutual.ui.screens.profile_post.ProfilePostScreen
import com.example.commutual.ui.screens.signup.SignUpScreen
import com.example.commutual.ui.screens.splash.SplashScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun CommutualApp() {
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
                commutualGraph(appState)
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
        CommutualAppState(scaffoldState, navController, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.commutualGraph(appState: CommutualAppState) {
    composable(SPLASH_SCREEN) {
        SplashScreen(
            openScreen = { route -> appState.navigate(route) },
            popUpScreen = { appState.popUp()},
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
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            openScreen = { route -> appState.navigate(route) })
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