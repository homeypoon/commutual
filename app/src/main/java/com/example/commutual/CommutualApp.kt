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
import com.example.commutual.ui.screens.chat.ChatScreen
import com.example.commutual.ui.screens.chat.MessagesScreen
import com.example.commutual.ui.screens.edit_post.EditPostScreen
import com.example.commutual.ui.screens.edit_profile.EditProfileScreen
import com.example.commutual.ui.screens.edit_task.EditTaskScreen
import com.example.commutual.ui.screens.explore.ExploreScreen
import com.example.commutual.ui.screens.home.HomeScreen
import com.example.commutual.ui.screens.login.LoginScreen
import com.example.commutual.ui.screens.post_details.PostDetailsScreen
import com.example.commutual.ui.screens.profile.ProfileScreen
import com.example.commutual.ui.screens.profile_post.ProfilePostScreen
import com.example.commutual.ui.screens.settings.SettingsScreen
import com.example.commutual.ui.screens.signup.SignUpScreen
import com.example.commutual.ui.screens.splash.SplashScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun CommutualApp() {
    val context = LocalContext.current


    Surface(
        color = MaterialTheme.colors.background
    ) {
        val appState = rememberAppState()
        val bottomNavState = rememberSaveable { (mutableStateOf(false)) }

        val navBackStackEntry by appState.navController.currentBackStackEntryAsState()

        LaunchedEffect(Unit) {
            appState.createNotificationChannel(context)
        }


        // Control bottomNav visibility
        when (navBackStackEntry?.destination?.route) {

            HOME_SCREEN -> {
                bottomNavState.value = true
            }
            EXPLORE_SCREEN -> {
                bottomNavState.value = true
            }
            CHAT_SCREEN -> {
                bottomNavState.value = true
            }
            PROFILE_SCREEN -> {
                bottomNavState.value = true
            }
            SETTINGS_SCREEN -> {
                bottomNavState.value = true
            }
            SPLASH_SCREEN -> {
                bottomNavState.value = false
            }
            EDIT_PROFILE_SCREEN -> {
                bottomNavState.value = false
            }
            PROFILE_POST_SCREEN -> {
                bottomNavState.value = false
            }
            LOGIN_SCREEN -> {
                bottomNavState.value = false
            }
            SIGN_UP_SCREEN -> {
                bottomNavState.value = false
            }
            "$EDIT_POST_SCREEN$POST_ID_ARG" -> {
                bottomNavState.value = false
            }
            "$POST_DETAILS_SCREEN$POST_ID_ARG" -> {
                bottomNavState.value = false
            }
            "$MESSAGES_SCREEN$CHAT_ID_ARG" -> {
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
            popUpScreen = { appState.popUp() },
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
        arguments = listOf(navArgument(POST_ID) { defaultValue = POST_DEFAULT_ID })
    ) {
        ProfilePostScreen(
            popUpScreen = { appState.popUp() },
            openScreen = { route -> appState.navigate(route) },
            postId = it.arguments?.getString(POST_ID) ?: POST_DEFAULT_ID
        )
    }

    composable(
        route = "$EDIT_POST_SCREEN$POST_ID_ARG$SCREEN_TITLE_ARG",
        arguments = listOf(
            navArgument(POST_ID) { defaultValue = POST_DEFAULT_ID },
            navArgument(SCREEN_TITLE) { defaultValue = ST_CREATE_POST }
        )
    ) {
        EditPostScreen(
            popUpScreen = { appState.popUp() },
            postId = it.arguments?.getString(POST_ID) ?: POST_DEFAULT_ID,
            screenTitle = it.arguments?.getString(SCREEN_TITLE) ?: ST_CREATE_POST
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

    composable(CHAT_SCREEN) {
        ChatScreen(
            openScreen = { route -> appState.navigate(route) })
    }

    composable(
        route = "$MESSAGES_SCREEN$CHAT_ID_ARG",
        arguments = listOf(navArgument(CHAT_ID) { defaultValue = CHAT_DEFAULT_ID })
    ) {
        MessagesScreen(
            openScreen = { route -> appState.navigate(route) },
            chatId = it.arguments?.getString(CHAT_ID) ?: CHAT_DEFAULT_ID,
        )
    }

    composable(
        route = "$PROFILE_SCREEN$USER_ID_ARG",
        arguments = listOf(navArgument(USER_ID) { defaultValue = USER_DEFAULT_ID })
    ) {

        ProfileScreen(
            openScreen = { route -> appState.navigate(route) },
            userId = it.arguments?.getString(USER_ID) ?: USER_DEFAULT_ID,
        )
    }

    composable(
        route = "$EDIT_TASK_SCREEN$TASK_ID_ARG$CHAT_ID_ARG$SCREEN_TITLE_ARG",
        arguments = listOf(
            navArgument(TASK_ID) { defaultValue = TASK_DEFAULT_ID },
            navArgument(CHAT_ID) { defaultValue = CHAT_DEFAULT_ID },
            navArgument(SCREEN_TITLE) { defaultValue = ST_CREATE_TASK }
        )
    ) {
        EditTaskScreen(
            popUpScreen = { appState.popUp() },
            taskId = it.arguments?.getString(TASK_ID) ?: TASK_DEFAULT_ID,
            chatId = it.arguments?.getString(CHAT_ID) ?: CHAT_DEFAULT_ID,
            screenTitle = it.arguments?.getString(SCREEN_TITLE) ?: ST_CREATE_TASK,
            setAlarmManager = appState::setAlarmManager
        )
    }

    composable(
        route = "$EDIT_PROFILE_SCREEN$SCREEN_TITLE_ARG",
        arguments = listOf(
            navArgument(SCREEN_TITLE) { defaultValue = ST_CREATE_PROFILE }
        )
    ) {
        EditProfileScreen(
            popUpScreen = { appState.popUp() },
            screenTitle = it.arguments?.getString(SCREEN_TITLE) ?: ST_CREATE_PROFILE
        )
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



    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) }
        )
    }

}